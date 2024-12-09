package com.example.foodplaza_traceability.infrastructure.out.jpa.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "traceability")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TraceabilityEntity {
    @Id
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
