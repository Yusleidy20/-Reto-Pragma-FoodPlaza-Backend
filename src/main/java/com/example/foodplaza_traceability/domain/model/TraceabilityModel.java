package com.example.foodplaza_traceability.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TraceabilityModel {
    private String idTraceability;
    private Long idOrder;
    private Long customerId;
    private String emailCustomer;
    private LocalDateTime date;
    private String previousState;
    private String newState;
    private Long employeeId;
    private String emailEmployee;
}
