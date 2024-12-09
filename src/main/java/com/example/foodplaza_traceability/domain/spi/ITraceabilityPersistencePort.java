package com.example.foodplaza_traceability.domain.spi;

import com.example.foodplaza_traceability.domain.model.TraceabilityModel;

import java.util.List;

public interface ITraceabilityPersistencePort {
    // Guardar trazabilidad en la base de datos
    TraceabilityModel saveTraceability(TraceabilityModel traceabilityModel);

    // Obtener trazabilidad por ID de pedido y cliente
    List<TraceabilityModel> findByOrderIdAndCustomerId(Long orderId, Long customerId);
    List<TraceabilityModel> findByOrderIds(List<Long> orderIds); // Cambiado
}
