package com.example.foodplaza_traceability.application.handler;

import com.example.foodplaza_traceability.application.dto.TraceabilityRequestDto;
import com.example.foodplaza_traceability.application.dto.TraceabilityResponseDto;

import java.util.List;

public interface ITraceabilityHandler {
    // Registrar un nuevo log de trazabilidad
    TraceabilityResponseDto logTraceability(TraceabilityRequestDto traceabilityRequestDto);

    // Consultar el historial de trazabilidad por ID de pedido y cliente
    List<TraceabilityResponseDto> getTraceabilityByOrder(Long orderId, Long customerId);
}
