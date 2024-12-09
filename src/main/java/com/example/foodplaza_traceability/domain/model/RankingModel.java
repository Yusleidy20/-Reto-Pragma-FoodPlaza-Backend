package com.example.foodplaza_traceability.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RankingModel {
    private Long employeeId;
    private List<EfficiencyModel> orders = new ArrayList<>();
    private double averageTime;


}
