package com.example.foodplaza.domain.usecase;

import com.example.foodplaza.domain.exception.UserNotExistException;
import com.example.foodplaza.domain.model.RestaurantModel;
import com.example.foodplaza.domain.spi.feignclients.IUserFeignClientPort;
import com.example.foodplaza.domain.spi.persistence.IRestaurantPersistencePort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseTest {

    @InjectMocks
    private RestaurantUseCase restaurantUseCase;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private IUserFeignClientPort userFeignClient;

    @Test
    void saveRestaurant_Success() {
        // Arrange
        RestaurantModel restaurantModel = new RestaurantModel(
                1L,
                "Restaurant 123",
                "123456789",
                "123 Main St",
                "+573005698325",
                "http://logo.com/logo.png",
                1L
        );

        Mockito.when(userFeignClient.existsUserById(1L)).thenReturn(true);
        Mockito.when(restaurantPersistencePort.saveRestaurant(restaurantModel)).thenReturn(restaurantModel);

        // Act
        restaurantUseCase.saveRestaurant(restaurantModel);

        // Assert
        Mockito.verify(userFeignClient).existsUserById(1L);
        Mockito.verify(restaurantPersistencePort).saveRestaurant(restaurantModel);
    }

    @Test
    void saveRestaurant_Fail_OwnerIdIsNull() {
        // Arrange
        RestaurantModel restaurantModel = new RestaurantModel(
                1L,
                "Restaurant 123",
                "123456789",
                "123 Main St",
                "+573005698325",
                "http://logo.com/logo.png",
                null // OwnerId is null
        );

        // Act & Assert
        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> restaurantUseCase.saveRestaurant(restaurantModel)
        );

        Assertions.assertEquals("The owner ID cannot be null.", exception.getMessage());
        Mockito.verify(userFeignClient, Mockito.never()).existsUserById(Mockito.any());
        Mockito.verify(restaurantPersistencePort, Mockito.never()).saveRestaurant(Mockito.any());
    }

    @Test
    void saveRestaurant_Fail_OwnerDoesNotExist() {
        // Arrange
        RestaurantModel restaurantModel = new RestaurantModel(
                1L,
                "Restaurant 123",
                "123456789",
                "123 Main St",
                "+573005698325",
                "http://logo.com/logo.png",
                99L // OwnerId does not exist
        );

        Mockito.when(userFeignClient.existsUserById(99L)).thenReturn(false);

        // Act & Assert
        UserNotExistException exception = Assertions.assertThrows(
                UserNotExistException.class,
                () -> restaurantUseCase.saveRestaurant(restaurantModel)
        );

        Assertions.assertEquals("The owner does not exist.", exception.getMessage());
        Mockito.verify(userFeignClient).existsUserById(99L);
        Mockito.verify(restaurantPersistencePort, Mockito.never()).saveRestaurant(Mockito.any());
    }
}
