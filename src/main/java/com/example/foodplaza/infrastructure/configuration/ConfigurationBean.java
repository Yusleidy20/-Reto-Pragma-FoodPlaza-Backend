package com.example.foodplaza.infrastructure.configuration;


import com.example.foodplaza.domain.api.IRestaurantServicePort;
import com.example.foodplaza.domain.spi.feignclients.IUserFeignClientPort;
import com.example.foodplaza.domain.spi.persistence.IRestaurantPersistencePort;
import com.example.foodplaza.domain.usecase.RestaurantUseCase;
import com.example.foodplaza.infrastructure.out.jpa.adapter.RestaurantAdapterImpl;
import com.example.foodplaza.infrastructure.out.jpa.feignclients.adapter.UserFeignAdapter;
import com.example.foodplaza.infrastructure.out.jpa.feignclients.mapper.IUserDtoMapper;
import com.example.foodplaza.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.example.foodplaza.infrastructure.out.jpa.repository.IRestaurantRepositoryMySql;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ConfigurationBean {
    private final IRestaurantRepositoryMySql restaurantRepository;

    private final IRestaurantEntityMapper restaurantEntityMapper;
    private final IUserFeignClient userFeignClient;
    private final IUserDtoMapper userDtoMapper;

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

}
