package com.example.foodplaza_traceability.application.handler;

import com.example.foodplaza_traceability.application.dto.TraceabilityRequestDto;
import com.example.foodplaza_traceability.application.dto.TraceabilityResponseDto;
import com.example.foodplaza_traceability.application.mapper.ITraceabilityRequestMapper;
import com.example.foodplaza_traceability.application.mapper.ITraceabilityResponseMapper;
import com.example.foodplaza_traceability.domain.api.ITraceabilityServicePort;
import com.example.foodplaza_traceability.domain.model.TraceabilityModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TraceabilityHandlerImpl implements ITraceabilityHandler {

    private final ITraceabilityServicePort traceabilityServicePort;
    private final ITraceabilityRequestMapper requestMapper;
    private final ITraceabilityResponseMapper responseMapper;

    @Override
    public TraceabilityResponseDto logTraceability(TraceabilityRequestDto requestDto) {
        TraceabilityModel model = requestMapper.toModel(requestDto);
        TraceabilityModel savedModel = traceabilityServicePort.logTraceability(model);
        return responseMapper.toDto(savedModel);
    }

    @Override
    public List<TraceabilityResponseDto> getTraceabilityByOrder(Long orderId, Long customerId) {
        return traceabilityServicePort.getTraceabilityByOrder(orderId, customerId).stream()
                .map(responseMapper::toDto)
                .toList();
    }
}
