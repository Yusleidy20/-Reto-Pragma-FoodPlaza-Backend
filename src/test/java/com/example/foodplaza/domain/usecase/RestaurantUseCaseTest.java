package com.example.foodplaza.domain.usecase;


import com.example.foodplaza.domain.exception.RestaurantValidationException;
import com.example.foodplaza.domain.exception.UserNotExistException;
import com.example.foodplaza.domain.model.RestaurantModel;
import com.example.foodplaza.domain.spi.feignclients.IUserFeignClientPort;
import com.example.foodplaza.domain.spi.persistence.IRestaurantPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseTest {

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private IUserFeignClientPort userFeignClient;

    @InjectMocks
    private RestaurantUseCase restaurantUseCase;

    private RestaurantModel restaurantModel;

    @BeforeEach
    void setUp() {
        // Configuración de un modelo base para pruebas
        restaurantModel = new RestaurantModel(
                1L,
                "Test Restaurant",
                "123456789",
                "123 Main Street",
                "555-1234",
                "http://test.com/logo.png",
                2L
        );
    }

    /**
     * Verifica que un restaurante se guarda correctamente cuando el propietario existe
     * y el NIT no está registrado.
     */
    @Test
    void saveRestaurant_shouldSaveRestaurant_whenOwnerExistsAndNitIsUnique() {
        // Configuración de mocks
        when(userFeignClient.existsUserById(restaurantModel.getOwnerId())).thenReturn(true);
        when(restaurantPersistencePort.existsByNit(restaurantModel.getNit())).thenReturn(false);
        when(restaurantPersistencePort.saveRestaurant(restaurantModel)).thenReturn(restaurantModel);

        // Acción
        restaurantUseCase.saveRestaurant(restaurantModel);

        // Verificaciones
        verify(userFeignClient).existsUserById(restaurantModel.getOwnerId());
        verify(restaurantPersistencePort).existsByNit(restaurantModel.getNit());
        verify(restaurantPersistencePort).saveRestaurant(restaurantModel);
    }

    /**
     * Verifica que se lanza una excepción si el propietario del restaurante no existe.
     */
    @Test
    void saveRestaurant_shouldThrowException_whenOwnerDoesNotExist() {
        // Configuración de mocks
        when(userFeignClient.existsUserById(restaurantModel.getOwnerId())).thenReturn(false);

        // Acción y verificación
        UserNotExistException exception = assertThrows(UserNotExistException.class, () -> {
            restaurantUseCase.saveRestaurant(restaurantModel);
        });

        assertEquals("The owner does not exist.", exception.getMessage());
        verify(userFeignClient).existsUserById(restaurantModel.getOwnerId());
        verifyNoInteractions(restaurantPersistencePort);
    }

    /**
     * Verifica que se lanza una excepción si el NIT del restaurante ya está registrado.
     */
    @Test
    void saveRestaurant_shouldThrowException_whenNitIsAlreadyRegistered() {
        // Configuración de mocks
        when(userFeignClient.existsUserById(restaurantModel.getOwnerId())).thenReturn(true);
        when(restaurantPersistencePort.existsByNit(restaurantModel.getNit())).thenReturn(true);

        // Acción y verificación
        RestaurantValidationException exception = assertThrows(RestaurantValidationException.class, () -> {
            restaurantUseCase.saveRestaurant(restaurantModel);
        });

        assertEquals("The NIT is already registered.", exception.getMessage());
        verify(restaurantPersistencePort).existsByNit(restaurantModel.getNit());
        verify(userFeignClient).existsUserById(restaurantModel.getOwnerId());
        verify(restaurantPersistencePort, never()).saveRestaurant(any());
    }

    /**
     * Verifica que un restaurante se recupera correctamente cuando existe por su ID.
     */
    @Test
    void getRestaurantById_shouldReturnRestaurant_whenRestaurantExists() {
        // Configuración de mocks
        when(restaurantPersistencePort.getRestaurantById(1L)).thenReturn(restaurantModel);

        // Acción
        RestaurantModel result = restaurantUseCase.getRestaurantById(1L);

        // Verificaciones
        assertEquals(restaurantModel, result);
        verify(restaurantPersistencePort).getRestaurantById(1L);
    }

    /**
     * Verifica que se puede recuperar un restaurante por el ID del propietario.
     */
    @Test
    void getRestaurantByIdOwner_shouldReturnRestaurant_whenOwnerHasARestaurant() {
        // Configuración de mocks
        when(restaurantPersistencePort.getRestaurantByIdOwner(2L)).thenReturn(restaurantModel);

        // Acción
        RestaurantModel result = restaurantUseCase.getRestaurantByIdOwner(2L);

        // Verificaciones
        assertEquals(restaurantModel, result);
        verify(restaurantPersistencePort).getRestaurantByIdOwner(2L);
    }

    /**
     * Verifica que el método retorna una lista vacía si no hay restaurantes registrados.
     */
    @Test
    void getAllRestaurants_shouldReturnEmptyList_whenNoRestaurantsExist() {
        // Configuración de mocks
        when(restaurantPersistencePort.getAllRestaurants()).thenReturn(Collections.emptyList());

        // Acción
        List<RestaurantModel> result = restaurantUseCase.getAllRestaurants();

        // Verificaciones
        assertTrue(result.isEmpty());
        verify(restaurantPersistencePort).getAllRestaurants();
    }

    /**
     * Verifica que el método para eliminar un restaurante invoca el puerto de persistencia.
     */
    @Test
    void deleteRestaurantById_shouldCallPersistencePort() {
        // Configuración de mocks
        when(restaurantPersistencePort.getRestaurantById(1L)).thenReturn(restaurantModel);

        // Acción
        restaurantUseCase.deleteRestaurantById(1L);

        // Verificaciones
        verify(restaurantPersistencePort).getRestaurantById(1L);
    }
}
