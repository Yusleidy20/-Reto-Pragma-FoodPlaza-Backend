package com.example.foodplaza.infrastructure.configuration;


import com.example.foodplaza.domain.api.ICategoryServicePort;
import com.example.foodplaza.domain.api.IDishServicePort;
import com.example.foodplaza.domain.api.IRestaurantServicePort;
import com.example.foodplaza.domain.spi.feignclients.IUserFeignClientPort;
import com.example.foodplaza.domain.spi.persistence.ICategoryPersistencePort;
import com.example.foodplaza.domain.spi.persistence.IDishPersistencePort;
import com.example.foodplaza.domain.spi.persistence.IRestaurantPersistencePort;
import com.example.foodplaza.domain.usecase.CategoryUseCase;
import com.example.foodplaza.domain.usecase.DishUseCase;
import com.example.foodplaza.domain.usecase.RestaurantUseCase;
import com.example.foodplaza.infrastructure.out.jpa.adapter.CategoryAdapterImpl;
import com.example.foodplaza.infrastructure.out.jpa.adapter.DishAdapterImpl;
import com.example.foodplaza.infrastructure.out.jpa.adapter.RestaurantAdapterImpl;
import com.example.foodplaza.infrastructure.out.jpa.feignclients.adapter.UserFeignAdapter;
import com.example.foodplaza.infrastructure.out.jpa.feignclients.mapper.IUserDtoMapper;
import com.example.foodplaza.infrastructure.out.jpa.feignclients.mapper.IUserFeignClient;
import com.example.foodplaza.infrastructure.out.jpa.mapper.ICategoryEntityMapper;
import com.example.foodplaza.infrastructure.out.jpa.mapper.IDishEntityMapper;
import com.example.foodplaza.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.example.foodplaza.infrastructure.out.jpa.repository.ICategoryRepository;
import com.example.foodplaza.infrastructure.out.jpa.repository.IDishRepository;
import com.example.foodplaza.infrastructure.out.jpa.repository.IRestaurantRepository;
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
    private final IRestaurantEntityMapper restaurantEntityMapper;
    private final IDishRepository dishRepository;
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
}
