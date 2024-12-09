package com.example.foodplaza_traceability.application.handler;

import com.example.foodplaza_traceability.domain.api.IEfficiencyServicePort;
import com.example.foodplaza_traceability.domain.model.EfficiencyModel;
import com.example.foodplaza_traceability.domain.model.RankingModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EfficiencyHandlerImpl implements IEfficiencyHandler {

    private final IEfficiencyServicePort efficiencyServicePort;

    @Override
    public List<EfficiencyModel> calculateOrderEfficiency(Long idRestaurant) {
        return efficiencyServicePort.calculateOrderEfficiency(idRestaurant);
    }

    @Override
    public List<RankingModel> calculateEmployeeRanking(Long idRestaurant) {
        return efficiencyServicePort.calculateEmployeeRanking(idRestaurant);
    }
}
