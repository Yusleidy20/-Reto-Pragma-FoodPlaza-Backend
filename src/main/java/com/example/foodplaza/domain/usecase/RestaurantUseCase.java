package com.example.foodplaza.domain.usecase;

import com.example.foodplaza.domain.api.IRestaurantServicePort;
import com.example.foodplaza.domain.exception.OwnerMustOnlyOwnARestaurantException;
import com.example.foodplaza.domain.exception.UserMustBeOwnerException;
import com.example.foodplaza.domain.exception.UserNotExistException;
import com.example.foodplaza.domain.model.RestaurantModel;
import com.example.foodplaza.domain.model.UserModel;
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
        log.info("Datos recibidos en RestaurantModel: {}", restaurantModel);

        // Validar que el propietario no sea nulo
        if (restaurantModel.getOwnerId() == null) {
            log.error("El ID del propietario no puede ser nulo.");
            throw new IllegalArgumentException("El ID del propietario no puede ser nulo.");
        }

        try {
            // Validar si el propietario existe
            boolean existUser = userFeignClient.existsUserById(restaurantModel.getOwnerId());
            if (!existUser) {
                log.error("El propietario con ID {} no existe.", restaurantModel.getOwnerId());
                throw new UserNotExistException("El propietario no existe.");
            }

            // Validar que el propietario no tenga otro restaurante
            RestaurantModel existingRestaurant = restaurantPersistencePort.getRestaurantByIdOwner(restaurantModel.getOwnerId());
            if (existingRestaurant != null) {
                log.error("El propietario con ID {} ya tiene un restaurante registrado.", restaurantModel.getOwnerId());
                throw new OwnerMustOnlyOwnARestaurantException("El propietario solo puede tener un restaurante.");
            }

            // Guardar el restaurante
            RestaurantModel savedRestaurant = restaurantPersistencePort.saveRestaurant(restaurantModel);
            log.info("Restaurante guardado exitosamente: {}", savedRestaurant);

        } catch (UserNotExistException | OwnerMustOnlyOwnARestaurantException e) {
            log.error("Error de validación al guardar restaurante: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al guardar restaurante: {}", e.getMessage(), e);
            throw new RuntimeException("Error al guardar el restaurante. Por favor, inténtelo de nuevo.");
        }
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
    public void deleteRestaurantById(Long id) {

    }
}
