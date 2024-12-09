package com.example.foodplaza_traceability.infrastructure.configuration;

import com.example.foodplaza_traceability.application.handler.EfficiencyHandlerImpl;
import com.example.foodplaza_traceability.application.handler.IEfficiencyHandler;
import com.example.foodplaza_traceability.application.handler.ITraceabilityHandler;
import com.example.foodplaza_traceability.application.handler.TraceabilityHandlerImpl;
import com.example.foodplaza_traceability.application.mapper.ITraceabilityRequestMapper;
import com.example.foodplaza_traceability.application.mapper.ITraceabilityResponseMapper;
import com.example.foodplaza_traceability.domain.api.IEfficiencyServicePort;
import com.example.foodplaza_traceability.domain.api.ITraceabilityServicePort;
import com.example.foodplaza_traceability.domain.spi.ITraceabilityPersistencePort;
import com.example.foodplaza_traceability.domain.usecase.EfficiencyUseCase;
import com.example.foodplaza_traceability.domain.usecase.TraceabilityUseCase;
import com.example.foodplaza_traceability.infrastructure.out.jpa.adapter.TraceabilityAdapterImpl;
import com.example.foodplaza_traceability.infrastructure.out.jpa.feignclients.mapper.IRestaurantFeignClient;
import com.example.foodplaza_traceability.infrastructure.out.jpa.mapper.ITraceabilityEntityMapper;
import com.example.foodplaza_traceability.infrastructure.out.jpa.repository.TraceabilityRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    // Bean para la implementación del puerto de persistencia
    @Bean
    public ITraceabilityPersistencePort traceabilityPersistencePort(TraceabilityRepository traceabilityRepository,
                                                                    ITraceabilityEntityMapper entityMapper) {
        return new TraceabilityAdapterImpl(traceabilityRepository, entityMapper);
    }

    // Bean para la implementación del caso de uso
    @Bean
    public ITraceabilityServicePort traceabilityServicePort(ITraceabilityPersistencePort traceabilityPersistencePort) {
        return new TraceabilityUseCase(traceabilityPersistencePort);
    }

    // Bean para el handler
    @Bean
    public ITraceabilityHandler traceabilityHandler(ITraceabilityServicePort traceabilityServicePort,
                                                    ITraceabilityRequestMapper requestMapper,
                                                    ITraceabilityResponseMapper responseMapper) {
        return new TraceabilityHandlerImpl(traceabilityServicePort, requestMapper, responseMapper);
    }

    @Bean
    public IEfficiencyHandler efficiencyHandler(IEfficiencyServicePort efficiencyServicePort) {
        return new EfficiencyHandlerImpl(efficiencyServicePort);
    }

    // Ya debes tener los beans necesarios para el servicio y caso de uso:
    @Bean
    public IEfficiencyServicePort efficiencyServicePort(ITraceabilityPersistencePort traceabilityPersistencePort, IRestaurantFeignClient restaurantFeignClient) {
        return new EfficiencyUseCase(traceabilityPersistencePort, restaurantFeignClient);
    }


}
