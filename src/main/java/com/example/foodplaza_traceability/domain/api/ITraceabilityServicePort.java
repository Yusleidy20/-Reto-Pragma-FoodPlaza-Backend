package com.example.foodplaza_traceability.domain.api;

import com.example.foodplaza_traceability.domain.model.TraceabilityModel;

import java.util.List;

public interface ITraceabilityServicePort {
    TraceabilityModel logTraceability(TraceabilityModel traceabilityModel);
    List<TraceabilityModel> getTraceabilityByOrder(Long orderId, Long customerId);
}
