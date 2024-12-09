package com.example.foodplaza.domain.api;

import com.example.foodplaza.application.dto.request.TraceabilityRequestDto;

public interface ITraceabilityServicePort {
    void logTraceability(TraceabilityRequestDto traceabilityRequestDto);
}
