package com.example.foodplaza_traceability.infrastructure.out.jpa.adapter;

import com.example.foodplaza_traceability.domain.model.TraceabilityModel;
import com.example.foodplaza_traceability.domain.spi.ITraceabilityPersistencePort;
import com.example.foodplaza_traceability.infrastructure.out.jpa.entity.TraceabilityEntity;
import com.example.foodplaza_traceability.infrastructure.out.jpa.mapper.ITraceabilityEntityMapper;
import com.example.foodplaza_traceability.infrastructure.out.jpa.repository.TraceabilityRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RequiredArgsConstructor
public class TraceabilityAdapterImpl implements ITraceabilityPersistencePort {
    private static final Logger log = LoggerFactory.getLogger(TraceabilityAdapterImpl.class);
    private final TraceabilityRepository traceabilityRepository;
    private final ITraceabilityEntityMapper traceabilityEntityMapper;

    @Override
    public TraceabilityModel saveTraceability(TraceabilityModel traceabilityModel) {
        TraceabilityEntity entity = traceabilityEntityMapper.toEntity(traceabilityModel);
        TraceabilityEntity savedEntity = traceabilityRepository.save(entity);
        return traceabilityEntityMapper.toModel(savedEntity);
    }

    @Override
    public List<TraceabilityModel> findByOrderIdAndCustomerId(Long orderId, Long customerId) {
        var entities = traceabilityRepository.findByIdOrderAndCustomerId(orderId, customerId);
        log.info("Fetched entities: {}", entities); // Verifica los datos recuperados
        return entities.stream()
                .map(traceabilityEntityMapper::toModel)
                .toList();
    }


    @Override
    public List<TraceabilityModel> findByOrderIds(List<Long> orderIds) {
        return traceabilityRepository.findByIdOrderIn(orderIds).stream()
                .map(traceabilityEntityMapper::toModel)
                .toList();
    }

}
