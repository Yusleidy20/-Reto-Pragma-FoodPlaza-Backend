package com.example.foodplaza.domain.usecase;

import com.example.foodplaza.domain.api.IRestaurantServicePort;

import com.example.foodplaza.domain.exception.UserNotExistException;
import com.example.foodplaza.domain.model.RestaurantModel;
import com.example.foodplaza.domain.spi.feignclients.IUserFeignClientPort;
import com.example.foodplaza.domain.spi.persistence.IRestaurantPersistencePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RestaurantUseCase implements IRestaurantServicePort {

    private static final Logger log = LoggerFactory.getLogger(RestaurantUseCase.class);
    private  final IRestaurantPersistencePort restaurantPersistencePort;

    private  final IUserFeignClientPort userFeignClient;
    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort, IUserFeignClientPort userFeignClient) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userFeignClient = userFeignClient;
    }

    @Override
    public void saveRestaurant(RestaurantModel restaurantModel) {
        log.info("Data received in RestaurantModel: {}", restaurantModel);

        // Validar que el propietario no sea nulo
        if (restaurantModel.getOwnerId() == null) {
            log.error("The owner ID cannot be null.");
            throw new IllegalArgumentException("The owner ID cannot be null.");
        }

        // Validar si el propietario existe
        boolean existUser = userFeignClient.existsUserById(restaurantModel.getOwnerId());
        if (!existUser) {
            log.error("The owner with ID {} does not exist.", restaurantModel.getOwnerId());
            throw new UserNotExistException("The owner not exist.");
        }

        RestaurantModel savedRestaurant = restaurantPersistencePort.saveRestaurant(restaurantModel);
        log.info("Restaurant saved successfully: {}", savedRestaurant);
    }



    @Override
    public RestaurantModel getRestaurantById(Long idRestaurant) {
        return restaurantPersistencePort.getRestaurantById(idRestaurant);
    }

    @Override
    public RestaurantModel getRestaurantByIdOwner(Long ownerId) {
        return restaurantPersistencePort.getRestaurantByIdOwner(ownerId);
    }

    @Override
    public List<RestaurantModel> getAllRestaurants() {
        return List.of();
    }

    @Override
    public List<RestaurantModel> getRestaurantsWithPagination(Integer page, Integer size) {
        return List.of();
    }

    @Override
    public void deleteRestaurantById(Long  idRestaurant) {
        restaurantPersistencePort.getRestaurantById( idRestaurant);
    }
}
