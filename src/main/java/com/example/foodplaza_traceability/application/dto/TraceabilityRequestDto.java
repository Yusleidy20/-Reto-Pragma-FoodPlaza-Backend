package com.example.foodplaza_traceability.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TraceabilityRequestDto {
    private Long idOrder;
    private Long customerId;
    private String emailCustomer;
    private String previousState;
    private String newState;
    private Long employeeId;
    private String emailEmployee;
}