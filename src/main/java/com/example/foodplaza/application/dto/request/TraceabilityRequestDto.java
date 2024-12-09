package com.example.foodplaza.application.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TraceabilityRequestDto {
    private Long idOrder;        // ID del pedido
    private Long customerId;     // ID del cliente
    private String emailCustomer; // Correo electrónico del cliente
    private String previousState; // Estado anterior del pedido
    private String newState;      // Nuevo estado del pedido
    private Long employeeId;      // ID del empleado que realizó el cambio
    private String emailEmployee; // Correo electrónico del empleado
}
