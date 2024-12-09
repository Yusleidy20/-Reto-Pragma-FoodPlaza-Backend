package com.example.foodplaza_traceability.application.handler;

import com.example.foodplaza_traceability.domain.model.EfficiencyModel;
import com.example.foodplaza_traceability.domain.model.RankingModel;

import java.util.List;

public interface IEfficiencyHandler {
    List<EfficiencyModel> calculateOrderEfficiency(Long idRestaurant);
    List<RankingModel> calculateEmployeeRanking(Long idRestaurant);
}
