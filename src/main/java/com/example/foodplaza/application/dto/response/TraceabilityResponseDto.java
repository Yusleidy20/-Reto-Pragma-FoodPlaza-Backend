package com.example.foodplaza.application.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TraceabilityResponseDto {
    private String idTraceability; // ID del registro de trazabilidad en MongoDB
    private Long idOrder;          // ID del pedido
    private LocalDateTime date;    // Fecha del registro de trazabilidad
    private String previousState;  // Estado anterior del pedido
    private String newState;       // Nuevo estado del pedido
    private String emailEmployee;  // Correo electrónico del empleado que realizó el cambio
}
