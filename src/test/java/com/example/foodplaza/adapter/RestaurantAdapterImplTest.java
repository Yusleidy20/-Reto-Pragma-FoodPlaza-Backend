package com.example.foodplaza.adapter;

import com.example.foodplaza.application.dto.response.RestaurantDto;
import com.example.foodplaza.domain.model.RestaurantModel;
import com.example.foodplaza.infrastructure.out.jpa.adapter.RestaurantAdapterImpl;
import com.example.foodplaza.infrastructure.out.jpa.entity.RestaurantEntity;
import com.example.foodplaza.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.example.foodplaza.infrastructure.out.jpa.repository.IRestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantAdapterImplTest {

    @Mock
    private IRestaurantRepository restaurantRepositoryMySql;

    @Mock
    private IRestaurantEntityMapper restaurantEntityMapper;

    @InjectMocks
    private RestaurantAdapterImpl restaurantAdapter;

    private RestaurantEntity restaurantEntity;
    private RestaurantModel restaurantModel;

    @BeforeEach
    void setUp() {
        // Configuración de objetos base para las pruebas
        restaurantEntity = new RestaurantEntity(
                1L,
                "Test Restaurant",
                "123456789",
                "123 Main Street",
                "555-1234",
                "http://test.com/logo.png",
                2L
        );

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
     * Verifica que un restaurante se guarda correctamente y se retorna el modelo esperado.
     */
    @Test
    void saveRestaurant_shouldSaveAndReturnRestaurantModel() {
        when(restaurantEntityMapper.toRestaurantEntity(restaurantModel)).thenReturn(restaurantEntity);
        when(restaurantRepositoryMySql.save(restaurantEntity)).thenReturn(restaurantEntity);
        when(restaurantEntityMapper.toRestaurantModel(restaurantEntity)).thenReturn(restaurantModel);

        RestaurantModel result = restaurantAdapter.saveRestaurant(restaurantModel);

        assertEquals(restaurantModel, result);
        verify(restaurantEntityMapper).toRestaurantEntity(restaurantModel);
        verify(restaurantRepositoryMySql).save(restaurantEntity);
        verify(restaurantEntityMapper).toRestaurantModel(restaurantEntity);
    }

    /**
     * Verifica que se obtienen restaurantes paginados y ordenados correctamente.
     */
    @Test
    void getRestaurantsWithPaginationAndSorting_shouldReturnPaginatedRestaurantDtos() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by("nameRestaurant").ascending());
        Page<RestaurantEntity> restaurantEntityPage = new PageImpl<>(List.of(restaurantEntity), pageable, 1);

        when(restaurantRepositoryMySql.findAll(pageable)).thenReturn(restaurantEntityPage);

        Page<RestaurantDto> result = restaurantAdapter.getRestaurantsWithPaginationAndSorting(pageable);

        assertEquals(1, result.getContent().size());
        assertEquals("Test Restaurant", result.getContent().get(0).getNameRestaurant());
        verify(restaurantRepositoryMySql).findAll(pageable);
    }

    /**
     * Verifica que se obtiene un restaurante por su ID y se retorna el modelo esperado.
     */
    @Test
    void getRestaurantById_shouldReturnRestaurantModel_whenExists() {
        when(restaurantRepositoryMySql.findById(1L)).thenReturn(Optional.of(restaurantEntity));
        when(restaurantEntityMapper.toRestaurantModel(restaurantEntity)).thenReturn(restaurantModel);

        RestaurantModel result = restaurantAdapter.getRestaurantById(1L);

        assertEquals(restaurantModel, result);
        verify(restaurantRepositoryMySql).findById(1L);
        verify(restaurantEntityMapper).toRestaurantModel(restaurantEntity);
    }

    /**
     * Verifica que se lanza una excepción si un restaurante no existe por su ID.
     */
    @Test
    void getRestaurantById_shouldThrowException_whenNotFound() {
        when(restaurantRepositoryMySql.findById(1L)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            restaurantAdapter.getRestaurantById(1L);
        });

        assertEquals("Restaurant with ID 1 not found.", exception.getMessage());
        verify(restaurantRepositoryMySql).findById(1L);
        verifyNoInteractions(restaurantEntityMapper);
    }

    /**
     * Verifica que se obtiene un restaurante por el ID del propietario y se retorna el modelo esperado.
     */
    @Test
    void getRestaurantByIdOwner_shouldReturnRestaurantModel_whenOwnerExists() {
        when(restaurantRepositoryMySql.findByOwnerId(2L)).thenReturn(Optional.of(restaurantEntity));
        when(restaurantEntityMapper.toRestaurantModel(restaurantEntity)).thenReturn(restaurantModel);

        RestaurantModel result = restaurantAdapter.getRestaurantByIdOwner(2L);

        assertEquals(restaurantModel, result);
        verify(restaurantRepositoryMySql).findByOwnerId(2L);
        verify(restaurantEntityMapper).toRestaurantModel(restaurantEntity);
    }

    /**
     * Verifica que se retorna un boolean indicando si un NIT existe en la base de datos.
     */
    @Test
    void existsByNit_shouldReturnTrue_whenNitExists() {
        when(restaurantRepositoryMySql.existsByNit("123456789")).thenReturn(true);

        boolean result = restaurantAdapter.existsByNit("123456789");

        assertTrue(result);
        verify(restaurantRepositoryMySql).existsByNit("123456789");
    }

    /**
     * Verifica que se obtiene la lista de todos los restaurantes.
     */
    @Test
    void getAllRestaurants_shouldReturnListOfRestaurantModels() {
        when(restaurantRepositoryMySql.findAll()).thenReturn(List.of(restaurantEntity));

        List<RestaurantModel> result = restaurantAdapter.getAllRestaurants();

        assertEquals(1, result.size());
        RestaurantModel actual = result.get(0);

        // Comparar campos individuales
        assertEquals(restaurantModel.getIdRestaurant(), actual.getIdRestaurant());
        assertEquals(restaurantModel.getNameRestaurant(), actual.getNameRestaurant());
        assertEquals(restaurantModel.getNit(), actual.getNit());
        assertEquals(restaurantModel.getAddress(), actual.getAddress());
        assertEquals(restaurantModel.getPhoneNumber(), actual.getPhoneNumber());
        assertEquals(restaurantModel.getUrlLogo(), actual.getUrlLogo());
        assertEquals(restaurantModel.getOwnerId(), actual.getOwnerId());
    }

    /**
     * Verifica que se retorna una lista de IDs de pedidos asociados a un restaurante.
     */
    @Test
    void getOrderIdsByRestaurantId_shouldReturnListOfOrderIds() {
        when(restaurantRepositoryMySql.findOrderIdsByRestaurantId(1L)).thenReturn(List.of(101L, 102L));

        List<Long> result = restaurantAdapter.getOrderIdsByRestaurantId(1L);

        assertEquals(2, result.size());
        assertTrue(result.contains(101L));
        verify(restaurantRepositoryMySql).findOrderIdsByRestaurantId(1L);
    }

    /**
     * Verifica que se valida correctamente si los platos pertenecen a un restaurante.
     */
    @Test
    void validateDishesBelongToRestaurant_shouldReturnTrue_whenDishesAreValid() {
        when(restaurantRepositoryMySql.findDishIdsByRestaurantId(1L)).thenReturn(List.of(201L, 202L));

        boolean result = restaurantAdapter.validateDishesBelongToRestaurant(1L, List.of(201L));

        assertTrue(result);
        verify(restaurantRepositoryMySql).findDishIdsByRestaurantId(1L);
    }
}
