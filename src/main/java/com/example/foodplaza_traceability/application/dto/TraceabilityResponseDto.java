package com.example.foodplaza_traceability.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TraceabilityResponseDto {
    private String idTraceability;
    private Long idOrder;
    private LocalDateTime date;
    private String previousState;
    private String newState;
    private String emailEmployee;
}