package com.example.foodplaza.domain.usecase;

import com.example.foodplaza.domain.exception.ResourceNotFoundException;
import com.example.foodplaza.domain.exception.UnauthorizedException;
import com.example.foodplaza.domain.model.DishModel;
import com.example.foodplaza.domain.spi.persistence.IDishPersistencePort;
import com.example.foodplaza.infrastructure.out.jpa.entity.RestaurantEntity;
import com.example.foodplaza.infrastructure.out.jpa.repository.IRestaurantRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
@ExtendWith(MockitoExtension.class)
class DishUseCaseTest {

    @InjectMocks
    private DishUseCase dishUseCase;

    @Mock
    private IDishPersistencePort dishPersistencePort;

    @Mock
    private IRestaurantRepository restaurantRepository;

    private DishModel dishModel;
    private DishModel existingDish;
    private RestaurantEntity restaurantEntity;

    @BeforeEach
    void setUp() {
        // Configuración inicial para los objetos de prueba
        dishModel = new DishModel(
                null,
                "Dish 1",
                10000,
                "Delicious dish",
                "http://example.com/dish.png",
                "Category 1",
                1L,
                true
        );

        existingDish = new DishModel(
                1L,
                "Dish 1",
                12000,
                "Updated dish",
                "http://example.com/dish.png",
                "Category 1",
                1L,
                true
        );

        restaurantEntity = new RestaurantEntity(
                1L,
                "Restaurant 1",
                "123456789",
                "123 Main St",
                "+573005698325",
                "http://logo.com/logo.png",
                1L
        );

        // Simular el contexto de seguridad con un usuario autenticado
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getDetails()).thenReturn(1L); // Simula el ID del usuario
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }


    // Test: Guardar un plato exitosamente
    @Test
    void saveDish_Success() {
        // Simula un restaurante válido y un plato a guardar
        Mockito.when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurantEntity));
        Mockito.when(dishPersistencePort.saveDish(dishModel)).thenReturn(dishModel);

        // Act: Llamada al caso de uso
        dishUseCase.saveDish(dishModel);

        // Assert: Verifica que el restaurante fue consultado y el plato fue guardado
        Mockito.verify(restaurantRepository).findById(1L);
        Mockito.verify(dishPersistencePort).saveDish(dishModel);
    }

    @Test
    void saveDish_Fail_UserNotOwner() {
        // Test: Fallo al guardar un plato porque el usuario no es propietario
        // Simula un restaurante cuyo propietario no es el usuario autenticado
        Mockito.when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurantEntity));
        restaurantEntity.setOwnerId(99L); // Cambiar el propietario para simular el error

        // Act & Assert: Se espera una excepción de tipo UnauthorizedException
        UnauthorizedException exception = Assertions.assertThrows(
                UnauthorizedException.class,
                () -> dishUseCase.saveDish(dishModel)
        );

        // Verifica el mensaje de la excepción y que no se haya guardado el plato
        Assertions.assertEquals("You are not authorized to perform this action on this restaurant.", exception.getMessage());
        Mockito.verify(restaurantRepository).findById(1L);
        Mockito.verifyNoInteractions(dishPersistencePort);
    }

    @Test
    void updateDish_Success() {
        // Test: Actualizar un plato exitosamente
        // Simula un plato existente y un restaurante válido
        Mockito.when(dishPersistencePort.getDishById(1L)).thenReturn(existingDish);
        Mockito.when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurantEntity));

        // Act: Llamada al caso de uso para actualizar el plato
        dishUseCase.updateDish(existingDish);

        // Assert: Verifica que el plato y el restaurante fueron consultados, y que el plato fue actualizado
        Mockito.verify(dishPersistencePort).getDishById(1L);
        Mockito.verify(restaurantRepository).findById(1L);
        Mockito.verify(dishPersistencePort).updateDish(existingDish);
    }

    @Test
    void updateDish_Fail_UserNotOwner() {
        // Test: Fallo al actualizar un plato porque el usuario no es propietario
        // Simula un plato existente y un restaurante cuyo propietario no es el usuario autenticado
        Mockito.when(dishPersistencePort.getDishById(1L)).thenReturn(existingDish);
        Mockito.when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurantEntity));
        restaurantEntity.setOwnerId(99L); // Cambiar el propietario para simular el error

        // Act & Assert: Se espera una excepción de tipo UnauthorizedException
        UnauthorizedException exception = Assertions.assertThrows(
                UnauthorizedException.class,
                () -> dishUseCase.updateDish(existingDish)
        );

        // Verifica el mensaje de la excepción y que no se haya actualizado el plato
        Assertions.assertEquals("You are not authorized to perform this action on this restaurant.", exception.getMessage());
        Mockito.verify(dishPersistencePort).getDishById(1L);
        Mockito.verify(restaurantRepository).findById(1L);
        Mockito.verifyNoMoreInteractions(dishPersistencePort);
    }

    @Test
    void updateDish_Fail_DishNotFound() {
        // Test: Fallo al actualizar un plato porque no se encuentra el plato
        // Simula que no se encuentra el plato en el repositorio
        Mockito.when(dishPersistencePort.getDishById(1L)).thenThrow(new ResourceNotFoundException("Dish not found"));

        // Act & Assert: Se espera una excepción de tipo ResourceNotFoundException
        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> dishUseCase.updateDish(existingDish)
        );

        // Verifica el mensaje de la excepción y que no se haya consultado el restaurante
        Assertions.assertEquals("Dish not found", exception.getMessage());
        Mockito.verify(dishPersistencePort).getDishById(1L);
        Mockito.verifyNoInteractions(restaurantRepository);
    }
}
