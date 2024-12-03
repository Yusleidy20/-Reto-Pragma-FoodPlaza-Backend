package com.example.foodplaza.infrastructure.out.jpa.adapter;

import com.example.foodplaza.domain.model.RestaurantModel;
import com.example.foodplaza.domain.spi.persistence.IRestaurantPersistencePort;
import com.example.foodplaza.infrastructure.out.jpa.entity.RestaurantEntity;
import com.example.foodplaza.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.example.foodplaza.infrastructure.out.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@RequiredArgsConstructor
public class RestaurantAdapterImpl implements IRestaurantPersistencePort {
    private final IRestaurantRepository restaurantRepositoryMySql;
    private  final IRestaurantEntityMapper restaurantEntityMapper;
    private static final Logger log = LoggerFactory.getLogger(RestaurantAdapterImpl.class);

    @Override
    public RestaurantModel saveRestaurant(RestaurantModel restaurantModel) {
        RestaurantEntity restaurantEntity = restaurantRepositoryMySql.save(restaurantEntityMapper.toRestaurantEntity(restaurantModel));
        log.info("Entity mapped before saving: {}", restaurantEntity);
        return restaurantEntityMapper.toRestaurantModel(restaurantEntity);
    }

    @Override
    public RestaurantModel getRestaurantById(Long idRestaurant) {
        Optional<RestaurantEntity> optionalRestaurantEntity = restaurantRepositoryMySql.findById(idRestaurant);
        RestaurantEntity restaurantEntity = optionalRestaurantEntity.orElse(null);
        return restaurantEntityMapper.toRestaurantModel(restaurantEntity);
    }

    @Override
    public RestaurantModel getRestaurantByIdOwner(Long ownerId) {
        Optional<RestaurantEntity> optionalRestaurantEntity = restaurantRepositoryMySql.findByOwnerId(ownerId);
        RestaurantEntity restaurantEntity = optionalRestaurantEntity.orElse(null);
        return restaurantEntityMapper.toRestaurantModel(restaurantEntity);
    }
}

