package com.example.foodplaza.domain.usecase;


import com.example.foodplaza.application.dto.response.RestaurantDto;
import com.example.foodplaza.domain.exception.RestaurantNotFoundException;
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
import org.springframework.data.domain.*;

import java.util.Arrays;
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
        when(userFeignClient.existsUserById(restaurantModel.getOwnerId())).thenReturn(true);
        when(restaurantPersistencePort.existsByNit(restaurantModel.getNit())).thenReturn(false);
        when(restaurantPersistencePort.saveRestaurant(restaurantModel)).thenReturn(restaurantModel);

        restaurantUseCase.saveRestaurant(restaurantModel);

        verify(userFeignClient).existsUserById(restaurantModel.getOwnerId());
        verify(restaurantPersistencePort).existsByNit(restaurantModel.getNit());
        verify(restaurantPersistencePort).saveRestaurant(restaurantModel);
    }

    /**
     * Verifica que se lanza una excepción si el propietario del restaurante no existe.
     */
    @Test
    void saveRestaurant_shouldThrowException_whenOwnerDoesNotExist() {
        when(userFeignClient.existsUserById(restaurantModel.getOwnerId())).thenReturn(false);

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
        when(userFeignClient.existsUserById(restaurantModel.getOwnerId())).thenReturn(true);
        when(restaurantPersistencePort.existsByNit(restaurantModel.getNit())).thenReturn(true);

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
        when(restaurantPersistencePort.getRestaurantById(1L)).thenReturn(restaurantModel);

        RestaurantModel result = restaurantUseCase.getRestaurantById(1L);

        assertEquals(restaurantModel, result);
        verify(restaurantPersistencePort).getRestaurantById(1L);
    }

    /**
     * Verifica que se puede recuperar un restaurante por el ID del propietario.
     */
    @Test
    void getRestaurantByIdOwner_shouldReturnRestaurant_whenOwnerHasARestaurant() {
        when(restaurantPersistencePort.getRestaurantByIdOwner(2L)).thenReturn(restaurantModel);

        RestaurantModel result = restaurantUseCase.getRestaurantByIdOwner(2L);

        assertEquals(restaurantModel, result);
        verify(restaurantPersistencePort).getRestaurantByIdOwner(2L);
    }

    /**
     * Verifica que el método retorna null si un propietario no tiene restaurantes asociados.
     */
    @Test
    void getRestaurantByIdOwner_shouldReturnNull_whenOwnerHasNoRestaurant() {
        when(restaurantPersistencePort.getRestaurantByIdOwner(2L)).thenReturn(null);

        RestaurantModel result = restaurantUseCase.getRestaurantByIdOwner(2L);

        assertNull(result);
        verify(restaurantPersistencePort).getRestaurantByIdOwner(2L);
    }

    /**
     * Verifica que el método retorna una lista vacía si no hay restaurantes registrados.
     */
    @Test
    void getAllRestaurants_shouldReturnEmptyList_whenNoRestaurantsExist() {
        when(restaurantPersistencePort.getAllRestaurants()).thenReturn(Collections.emptyList());

        List<RestaurantModel> result = restaurantUseCase.getAllRestaurants();

        assertTrue(result.isEmpty());
        verify(restaurantPersistencePort).getAllRestaurants();
    }

    /**
     * Verifica que el método para eliminar un restaurante lanza una excepción si no existe.
     */
    @Test
    void deleteRestaurantById_shouldThrowException_whenRestaurantDoesNotExist() {
        when(restaurantPersistencePort.getRestaurantById(1L)).thenThrow(new RestaurantNotFoundException("Restaurant not found."));

        RestaurantNotFoundException exception = assertThrows(RestaurantNotFoundException.class, () -> {
            restaurantUseCase.deleteRestaurantById(1L);
        });

        assertEquals("Restaurant not found.", exception.getMessage());
        verify(restaurantPersistencePort).getRestaurantById(1L);
        verifyNoMoreInteractions(restaurantPersistencePort);
    }

    /**
     * Verifica que se obtienen restaurantes paginados y ordenados correctamente.
     */
    @Test
    void getRestaurantsWithPaginationAndSorting_shouldReturnFilteredRestaurants() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by("nameRestaurant").ascending());
        List<RestaurantDto> restaurants = Arrays.asList(
                new RestaurantDto("Test Restaurant 1", "http://test.com/logo1.png"),
                new RestaurantDto("Test Restaurant 2", "http://test.com/logo2.png")
        );
        Page<RestaurantDto> restaurantPage = new PageImpl<>(restaurants, pageable, restaurants.size());

        when(restaurantPersistencePort.getRestaurantsWithPaginationAndSorting(pageable)).thenReturn(restaurantPage);

        Page<RestaurantDto> result = restaurantUseCase.getRestaurantsWithPaginationAndSorting(0, 2, "nameRestaurant");

        assertEquals(2, result.getContent().size());
        assertEquals("Test Restaurant 1", result.getContent().get(0).getNameRestaurant());
        verify(restaurantPersistencePort).getRestaurantsWithPaginationAndSorting(pageable);
    }
}
