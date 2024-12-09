package com.example.foodplaza.adapter;

import com.example.foodplaza.domain.exception.ResourceNotFoundException;
import com.example.foodplaza.domain.model.DishModel;
import com.example.foodplaza.infrastructure.out.jpa.adapter.DishAdapterImpl;
import com.example.foodplaza.infrastructure.out.jpa.entity.DishEntity;
import com.example.foodplaza.infrastructure.out.jpa.entity.RestaurantEntity;
import com.example.foodplaza.infrastructure.out.jpa.mapper.IDishEntityMapper;
import com.example.foodplaza.infrastructure.out.jpa.repository.IDishRepository;
import com.example.foodplaza.infrastructure.out.jpa.repository.IRestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishAdapterImplTest {

    @Mock
    private IDishRepository dishRepository;

    @Mock
    private IDishEntityMapper dishEntityMapper;

    @Mock
    private IRestaurantRepository restaurantRepository;

    @InjectMocks
    private DishAdapterImpl dishAdapter;

    private DishEntity dishEntity;
    private DishModel dishModel;
    private RestaurantEntity restaurantEntity;

    @BeforeEach
    void setUp() {
        restaurantEntity = new RestaurantEntity();
        restaurantEntity.setIdRestaurant(1L);

        dishEntity = new DishEntity();
        dishEntity.setIdDish(1L);
        dishEntity.setNameDish("Test Dish");
        dishEntity.setPrice(100);
        dishEntity.setDescription("Test Description");
        dishEntity.setUrlImage("http://test.com/image.png");
        dishEntity.setActive(true);
        dishEntity.setIdRestaurant(restaurantEntity);

        dishModel = new DishModel();
        dishModel.setIdDish(1L);
        dishModel.setNameDish("Test Dish");
        dishModel.setPrice(100);
        dishModel.setDescription("Test Description");
        dishModel.setUrlImage("http://test.com/image.png");
        dishModel.setActive(true);
        dishModel.setIdRestaurant(1L);
    }

    /**
     * Verifica que un plato se guarda correctamente en la base de datos.
     */
    @Test
    void saveDish_shouldSaveDishAndReturnDishModel() {
        when(restaurantRepository.findById(dishModel.getIdRestaurant())).thenReturn(Optional.of(restaurantEntity));
        when(dishEntityMapper.toDishEntity(dishModel)).thenReturn(dishEntity);
        when(dishRepository.save(dishEntity)).thenReturn(dishEntity);
        when(dishEntityMapper.toDishModel(dishEntity)).thenReturn(dishModel);

        DishModel result = dishAdapter.saveDish(dishModel);

        assertEquals(dishModel, result);
        verify(restaurantRepository).findById(dishModel.getIdRestaurant());
        verify(dishRepository).save(dishEntity);
        verify(dishEntityMapper).toDishModel(dishEntity);
    }

    /**
     * Verifica que se lanza una excepción si el restaurante no existe al guardar un plato.
     */
    @Test
    void saveDish_shouldThrowException_whenRestaurantDoesNotExist() {
        when(restaurantRepository.findById(dishModel.getIdRestaurant())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            dishAdapter.saveDish(dishModel);
        });

        assertEquals("Restaurant not found with ID: 1", exception.getMessage());
        verify(restaurantRepository).findById(dishModel.getIdRestaurant());
        verifyNoInteractions(dishRepository);
        verifyNoInteractions(dishEntityMapper);
    }

    /**
     * Verifica que un plato se obtiene correctamente por su ID.
     */
    @Test
    void getDishById_shouldReturnDishModel_whenDishExists() {
        when(dishRepository.findById(1L)).thenReturn(Optional.of(dishEntity));
        when(dishEntityMapper.toDishModel(dishEntity)).thenReturn(dishModel);

        DishModel result = dishAdapter.getDishById(1L);

        assertEquals(dishModel, result);
        verify(dishRepository).findById(1L);
        verify(dishEntityMapper).toDishModel(dishEntity);
    }

    /**
     * Verifica que se lanza una excepción si el plato no existe por su ID.
     */
    @Test
    void getDishById_shouldThrowException_whenDishDoesNotExist() {
        when(dishRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            dishAdapter.getDishById(1L);
        });

        assertEquals("Dish not found with ID: 1", exception.getMessage());
        verify(dishRepository).findById(1L);
    }

    /**
     * Verifica que un plato se actualiza correctamente en la base de datos.
     */
    @Test
    void updateDish_shouldUpdateDishSuccessfully() {
        when(dishRepository.findById(dishModel.getIdDish())).thenReturn(Optional.of(dishEntity));

        dishAdapter.updateDish(dishModel);

        assertEquals(dishModel.getPrice(), dishEntity.getPrice());
        assertEquals(dishModel.getDescription(), dishEntity.getDescription());
        assertEquals(dishModel.getActive(), dishEntity.getActive());
        verify(dishRepository).save(dishEntity);
    }

    /**
     * Verifica que se lanza una excepción si el plato no existe al intentar actualizarlo.
     */
    @Test
    void updateDish_shouldThrowException_whenDishDoesNotExist() {
        when(dishRepository.findById(dishModel.getIdDish())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            dishAdapter.updateDish(dishModel);
        });

        assertEquals("Dish not found with ID: 1", exception.getMessage());
        verify(dishRepository).findById(dishModel.getIdDish());
    }

    /**
     * Verifica que un plato se elimina correctamente por su ID.
     */
    @Test
    void deleteDish_shouldDeleteDishById() {
        dishAdapter.deleteDish(1L);

        verify(dishRepository).deleteById(1L);
    }

    /**
     * Verifica que se listan los platos correctamente con paginación y filtrado por categoría.
     */
    @Test
    void listDishes_shouldReturnPaginatedDishes_whenCategoryAndRestaurantProvided() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<DishEntity> dishEntities = new PageImpl<>(List.of(dishEntity), pageable, 1);

        when(dishRepository.findByIdRestaurantAndIdCategory(1L, 1L, pageable)).thenReturn(dishEntities);
        when(dishEntityMapper.toDishModel(dishEntity)).thenReturn(dishModel);

        Page<DishModel> result = dishAdapter.listDishes(pageable, 1L, 1L);

        assertEquals(1, result.getContent().size());
        assertEquals(dishModel, result.getContent().get(0));
        verify(dishRepository).findByIdRestaurantAndIdCategory(1L, 1L, pageable);
    }

    /**
     * Verifica que se listan los platos correctamente con paginación solo por restaurante.
     */
    @Test
    void listDishes_shouldReturnPaginatedDishes_whenOnlyRestaurantProvided() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<DishEntity> dishEntities = new PageImpl<>(List.of(dishEntity), pageable, 1);

        when(dishRepository.findByIdRestaurant(1L, pageable)).thenReturn(dishEntities);
        when(dishEntityMapper.toDishModel(dishEntity)).thenReturn(dishModel);

        Page<DishModel> result = dishAdapter.listDishes(pageable, null, 1L);

        assertEquals(1, result.getContent().size());
        assertEquals(dishModel, result.getContent().get(0));
        verify(dishRepository).findByIdRestaurant(1L, pageable);
    }
}
