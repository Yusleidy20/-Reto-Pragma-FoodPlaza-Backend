package com.example.foodplaza.infrastructure.configuration;


import com.example.foodplaza.application.handler.IOrderHandlerPort;
import com.example.foodplaza.application.handler.impl.OrderHandlerImpl;
import com.example.foodplaza.application.mapper.request.IOrderRequestMapper;
import com.example.foodplaza.application.mapper.response.IOrderResponseMapper;
import com.example.foodplaza.domain.api.*;
import com.example.foodplaza.domain.spi.feignclients.IUserFeignClientPort;
import com.example.foodplaza.domain.spi.persistence.*;
import com.example.foodplaza.domain.usecase.*;
import com.example.foodplaza.infrastructure.out.jpa.adapter.*;
import com.example.foodplaza.infrastructure.out.jpa.feignclients.adapter.UserFeignAdapter;
import com.example.foodplaza.infrastructure.out.jpa.feignclients.mapper.IUserDtoMapper;
import com.example.foodplaza.infrastructure.out.jpa.feignclients.mapper.IUserFeignClient;
import com.example.foodplaza.infrastructure.out.jpa.mapper.*;
import com.example.foodplaza.infrastructure.out.jpa.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ConfigurationBean {
    private final IRestaurantRepository restaurantRepository;
    private final IDishRepository dishRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;
    private final IOrderDishRepository orderDishRepository;
    private final IDishEntityMapper dishEntityMapper;
    private final IUserFeignClient userFeignClient;
    private final IUserDtoMapper userDtoMapper;
    private final ICategoryRepository categoryRepository;
    private final ICategoryEntityMapper categoryEntityMapper;
    @Bean
    public IRestaurantPersistencePort restaurantPersistencePort(){
        return new RestaurantAdapterImpl(restaurantRepository, restaurantEntityMapper);
    }

    @Bean
    public IUserFeignClientPort userFeignClientPort(){
        return new UserFeignAdapter(userFeignClient, userDtoMapper);
    }

    @Bean
    public IRestaurantServicePort restaurantServicePort() {
        return new RestaurantUseCase(restaurantPersistencePort(), userFeignClientPort());
    }
    @Bean
    public IDishPersistencePort dishPersistencePort() {
        return new DishAdapterImpl(dishRepository, dishEntityMapper,restaurantRepository);
    }

    @Bean
    public IDishServicePort dishServicePort() {
        return new DishUseCase(dishPersistencePort(), restaurantPersistencePort());
    }

    @Bean
    public ICategoryPersistencePort categoryPersistencePort(){
        return new CategoryAdapterImpl(categoryRepository, categoryEntityMapper);
    }

    @Bean
    public ICategoryServicePort categoryServicePort(){
        return new CategoryUseCase(categoryPersistencePort());
    }
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }
    @Bean
    public IValidatorServicePort validatorServicePort(IOrderPersistencePort orderPersistencePort) {
        return new ValidatorService(orderPersistencePort);
    }

    @Bean
    public IOrderServicePort orderServicePort(IOrderPersistencePort orderPersistencePort,
                                              IOrderDishPersistencePort orderDishPersistencePort,
                                              IValidatorServicePort validatorServicePort) {
        return new OrderUseCase(orderPersistencePort, orderDishPersistencePort, validatorServicePort);
    }


    @Bean
    public IOrderPersistencePort orderPersistencePort(IOrderRepository orderRepository,
                                                      IOrderEntityMapper orderEntityMapper) {
        return new OrderAdapterImpl(orderRepository,dishRepository ,orderEntityMapper);
    }

    @Bean
    public IOrderDishPersistencePort orderDishPersistencePort(IOrderDishRepository orderDishRepository,
                                                              IOrderDishEntityMapper orderDishEntityMapper) {
        return new OrderDishAdapterImpl(orderDishRepository, orderDishEntityMapper);
    }

}
