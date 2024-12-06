package com.example.foodplaza.domain.usecase;

import com.example.foodplaza.domain.exception.ResourceNotFoundException;
import com.example.foodplaza.domain.exception.UnauthorizedException;
import com.example.foodplaza.domain.model.DishModel;
import com.example.foodplaza.domain.spi.persistence.IDishPersistencePort;
import com.example.foodplaza.infrastructure.out.jpa.entity.RestaurantEntity;
import com.example.foodplaza.infrastructure.out.jpa.repository.IRestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.*;

import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

 class DishUseCaseTest {

    @Mock
    private IDishPersistencePort dishPersistencePort;

    @Mock
    private IRestaurantRepository restaurantRepository;

    @InjectMocks
    private DishUseCase dishUseCase;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testSaveDish_Success() {
        // Arrange
        DishModel dishModel = new DishModel();
        dishModel.setIdRestaurant(1L);
        dishModel.setPrice(100000);
        dishModel.setDescription("Delicious dish");

        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setOwnerId(1L);
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getDetails()).thenReturn(1L); // Simulando que el usuario autenticado es el propietario del restaurante

        // Act
        dishUseCase.saveDish(dishModel);

        // Assert
        verify(dishPersistencePort, times(1)).saveDish(dishModel);
    }

    @Test
     void testSaveDish_Unauthorized() {
        // Arrange
        DishModel dishModel = new DishModel();
        dishModel.setIdRestaurant(1L);

        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setOwnerId(2L); // no es el propietario autenticado
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getDetails()).thenReturn(1L); //no es el propietario

        // Act & Assert
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            dishUseCase.saveDish(dishModel);
        });
        assertEquals("You are not authorized to perform this action on this restaurant.", exception.getMessage());
    }

    @Test
    @Transactional
     void testUpdateDish_Success() {
        // Arrange
        DishModel dishModel = new DishModel();
        dishModel.setIdDish(1L);
        dishModel.setPrice(12000);
        dishModel.setDescription("Updated description");

        DishModel existingDish = new DishModel();
        existingDish.setIdDish(1L);
        existingDish.setIdRestaurant(1L);
        existingDish.setPrice(10000);
        existingDish.setDescription("Old description");

        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setOwnerId(1L); // El propietario si es el autenticado
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        when(dishPersistencePort.getDishById(1L)).thenReturn(existingDish);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getDetails()).thenReturn(1L); // es el propietario autenticado

        // Act
        dishUseCase.updateDish(dishModel);

        // Assert
        assertEquals(12000, existingDish.getPrice());
        assertEquals("Updated description", existingDish.getDescription());
        verify(dishPersistencePort, times(1)).updateDish(existingDish);
    }

    @Test
     void testValidateOwner_Success() {
        // Arrange
        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setOwnerId(1L);
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getDetails()).thenReturn(1L); // Simulando que el usuario autenticado es el propietario

        // Act
        dishUseCase.validateOwner(1L, 1L);

        // Assert
        // No exceptions should be thrown, so this is considered a success.
    }

    @Test
     void testValidateOwner_Unauthorized() {
        // Arrange
        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setOwnerId(2L); // El propietario del restaurante no es el usuario autenticado
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getDetails()).thenReturn(1L); // El usuario autenticado no es el propietario

        // Act & Assert
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            dishUseCase.validateOwner(1L, 1L);
        });
        assertEquals("You are not authorized to perform this action on this restaurant.", exception.getMessage());
    }
}
