package com.example.foodplaza_traceability.domain.api;

import com.example.foodplaza_traceability.domain.model.EfficiencyModel;
import com.example.foodplaza_traceability.domain.model.RankingModel;

import java.util.List;

public interface IEfficiencyServicePort {
    List<EfficiencyModel> calculateOrderEfficiency(Long idRestaurant);
    List<RankingModel> calculateEmployeeRanking(Long idRestaurant);
}
