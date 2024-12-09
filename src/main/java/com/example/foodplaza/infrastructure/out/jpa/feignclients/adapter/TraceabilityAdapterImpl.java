package com.example.foodplaza.infrastructure.out.jpa.feignclients.adapter;

import com.example.foodplaza.application.dto.request.TraceabilityRequestDto;
import com.example.foodplaza.domain.api.ITraceabilityServicePort;
import com.example.foodplaza.infrastructure.out.jpa.feignclients.mapper.ITraceabilityFeignClient;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TraceabilityAdapterImpl implements ITraceabilityServicePort {

    private final ITraceabilityFeignClient traceabilityFeignClient;

    @Override
    public void logTraceability(TraceabilityRequestDto traceabilityRequestDto) {
        try {
            traceabilityFeignClient.logTraceability(traceabilityRequestDto);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to log traceability: " + e.getMessage(), e);
        }
    }
}
