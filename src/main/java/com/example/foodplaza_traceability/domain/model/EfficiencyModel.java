package com.example.foodplaza_traceability.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EfficiencyModel {
    private Long idOrder;     // ID del pedido
    private double minutes;   // Tiempo en minutos
    private double hours;     // Tiempo en horas
}
