package com.example.foodplaza_traceability.domain.usecase;

import com.example.foodplaza_traceability.domain.api.ITraceabilityServicePort;
import com.example.foodplaza_traceability.domain.model.TraceabilityModel;
import com.example.foodplaza_traceability.domain.spi.ITraceabilityPersistencePort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TraceabilityUseCase implements ITraceabilityServicePort {

    private final ITraceabilityPersistencePort traceabilityPersistencePort;

    @Override
    public TraceabilityModel logTraceability(TraceabilityModel traceabilityModel) {
        // Validar datos obligatorios
        if (traceabilityModel.getIdOrder() == null || traceabilityModel.getCustomerId() == null) {
            throw new IllegalArgumentException("Order ID and Customer ID are required.");
        }

        // Registrar trazabilidad
        return traceabilityPersistencePort.saveTraceability(traceabilityModel);
    }

    @Override
    public List<TraceabilityModel> getTraceabilityByOrder(Long orderId, Long customerId) {
        return traceabilityPersistencePort.findByOrderIdAndCustomerId(orderId, customerId);
    }
}
