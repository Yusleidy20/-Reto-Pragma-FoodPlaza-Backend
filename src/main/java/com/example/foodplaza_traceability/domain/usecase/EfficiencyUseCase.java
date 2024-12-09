package com.example.foodplaza_traceability.domain.usecase;

import com.example.foodplaza_traceability.domain.api.IEfficiencyServicePort;
import com.example.foodplaza_traceability.domain.model.EfficiencyModel;
import com.example.foodplaza_traceability.domain.model.RankingModel;
import com.example.foodplaza_traceability.domain.model.TraceabilityModel;
import com.example.foodplaza_traceability.domain.spi.ITraceabilityPersistencePort;
import com.example.foodplaza_traceability.infrastructure.configuration.Constants;
import com.example.foodplaza_traceability.infrastructure.out.jpa.feignclients.RestaurantDto;
import com.example.foodplaza_traceability.infrastructure.out.jpa.feignclients.mapper.IRestaurantFeignClient;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class EfficiencyUseCase implements IEfficiencyServicePort {

    private final ITraceabilityPersistencePort traceabilityPersistencePort;
    private final IRestaurantFeignClient restaurantFeignClient;

    @Override
    public List<EfficiencyModel> calculateOrderEfficiency(Long restaurantId) {
        List<Long> orderIds = fetchOrderIdsForRestaurant(restaurantId);
        if (orderIds.isEmpty()) return List.of();

        List<TraceabilityModel> traces = traceabilityPersistencePort.findByOrderIds(orderIds);
        return calculateOrderEfficiencies(traces);
    }

    private List<EfficiencyModel> calculateOrderEfficiencies(List<TraceabilityModel> traces) {
        return traces.stream()
                .collect(Collectors.groupingBy(TraceabilityModel::getIdOrder))
                .entrySet()
                .stream()
                .map(this::calculateOrderEfficiency)
                .filter(Objects::nonNull)
                .toList(); // Cambiado de collect(Collectors.toList()) a toList()
    }

    private EfficiencyModel calculateOrderEfficiency(Map.Entry<Long, List<TraceabilityModel>> entry) {
        Long orderId = entry.getKey();
        List<TraceabilityModel> orderTraces = entry.getValue();

        TraceabilityModel startTrace = findTraceByState(orderTraces, Constants.IN_PREPARATION);
        TraceabilityModel endTrace = findTraceByState(orderTraces, Constants.DELIVERED);

        if (startTrace != null && endTrace != null) {
            return createEfficiencyModel(orderId, startTrace, endTrace);
        }
        return null;
    }

    private EfficiencyModel createEfficiencyModel(Long orderId, TraceabilityModel startTrace, TraceabilityModel endTrace) {
        long durationInMinutes = calculateDurationInMinutes(startTrace.getDate(), endTrace.getDate());
        double durationInHours =  durationInMinutes / 60.0;
        return new EfficiencyModel(orderId, durationInMinutes, durationInHours);
    }

    private long calculateDurationInMinutes(LocalDateTime startDate, LocalDateTime endDate) {
        Duration duration = Duration.between(startDate, endDate);
        return duration.toMinutes();
    }

    @Override
    public List<RankingModel> calculateEmployeeRanking(Long restaurantId) {
        List<Long> orderIds = fetchOrderIdsForRestaurant(restaurantId);
        if (orderIds.isEmpty()) return List.of();

        List<TraceabilityModel> traces = traceabilityPersistencePort.findByOrderIds(orderIds);
        return calculateEmployeeRankingsFromTraces(traces);
    }

    private List<RankingModel> calculateEmployeeRankingsFromTraces(List<TraceabilityModel> traces) {
        return traces.stream()
                .filter(trace -> trace.getEmployeeId() != null)
                .collect(Collectors.groupingBy(TraceabilityModel::getEmployeeId))
                .entrySet()
                .stream()
                .map(this::calculateEmployeeRanking)
                .toList(); // Cambiado de collect(Collectors.toList()) a toList()
    }

    private RankingModel calculateEmployeeRanking(Map.Entry<Long, List<TraceabilityModel>> entry) {
        Long employeeId = entry.getKey();
        List<TraceabilityModel> employeeTraces = entry.getValue();

        List<EfficiencyModel> efficiencies = calculateOrderEfficienciesForEmployee(employeeTraces);
        double averageTime = calculateAverageTime(efficiencies);

        return new RankingModel(employeeId, efficiencies, averageTime);
    }

    private List<EfficiencyModel> calculateOrderEfficienciesForEmployee(List<TraceabilityModel> employeeTraces) {
        return employeeTraces.stream()
                .collect(Collectors.groupingBy(TraceabilityModel::getIdOrder))
                .entrySet()
                .stream()
                .map(this::calculateOrderEfficiency)
                .filter(Objects::nonNull)
                .toList(); // Cambiado de collect(Collectors.toList()) a toList()
    }

    private double calculateAverageTime(List<EfficiencyModel> efficiencies) {
        return efficiencies.stream()
                .mapToDouble(EfficiencyModel::getMinutes)
                .average()
                .orElse(0.0);
    }

    private List<Long> fetchOrderIdsForRestaurant(Long restaurantId) {
        List<Long> orderIds = restaurantFeignClient.getOrderIdsByRestaurantId(restaurantId);
        return orderIds.isEmpty() ? List.of() : orderIds;
    }

    private TraceabilityModel findTraceByState(List<TraceabilityModel> traces, String state) {
        return traces.stream()
                .filter(trace -> state.equalsIgnoreCase(trace.getNewState()))
                .findFirst()
                .orElse(null);
    }

    public RestaurantDto getRestaurantById(Long restaurantId) {
        RestaurantDto restaurant = restaurantFeignClient.getRestaurantById(restaurantId);
        if (restaurant == null) {
            throw new IllegalArgumentException("El restaurante con el ID proporcionado no existe.");
        }
        return restaurant;
    }
}
