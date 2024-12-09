package com.example.foodplaza_traceability.infrastructure.input.rest;

import com.example.foodplaza_traceability.application.handler.IEfficiencyHandler;
import com.example.foodplaza_traceability.domain.model.EfficiencyModel;
import com.example.foodplaza_traceability.domain.model.RankingModel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping(path = "/traceability-micro/")
@RequiredArgsConstructor
public class EfficiencyRestController {
    private final IEfficiencyHandler efficiencyHandler;
    private static final Logger log = LoggerFactory.getLogger(EfficiencyRestController.class);
    @GetMapping("/efficiency/orders/{idRestaurant}")
    @PreAuthorize("hasAuthority('ROLE_Owner')")
    public ResponseEntity<List<EfficiencyModel>> getOrderEfficiency(@PathVariable Long idRestaurant) {
        log.info("Authentication: {}", SecurityContextHolder.getContext().getAuthentication());
        List<EfficiencyModel> efficiencies = efficiencyHandler.calculateOrderEfficiency(idRestaurant);
        return ResponseEntity.ok(efficiencies);
    }


    @GetMapping("/efficiency/employees/{idRestaurant}")
    @PreAuthorize("hasAuthority('ROLE_Owner')")
    public ResponseEntity<List<RankingModel>> getEmployeeRanking(@PathVariable Long idRestaurant) {
        List<RankingModel> rankings = efficiencyHandler.calculateEmployeeRanking(idRestaurant);
        return ResponseEntity.ok(rankings);
    }

}
