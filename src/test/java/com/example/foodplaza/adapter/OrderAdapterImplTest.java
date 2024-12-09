package com.example.foodplaza.adapter;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.foodplaza.domain.model.OrderModel;
import com.example.foodplaza.infrastructure.configuration.Constants;
import com.example.foodplaza.infrastructure.out.jpa.adapter.OrderAdapterImpl;
import com.example.foodplaza.infrastructure.out.jpa.entity.DishEntity;
import com.example.foodplaza.infrastructure.out.jpa.entity.OrderEntity;
import com.example.foodplaza.infrastructure.out.jpa.entity.RestaurantEntity;
import com.example.foodplaza.infrastructure.out.jpa.mapper.IOrderEntityMapper;
import com.example.foodplaza.infrastructure.out.jpa.repository.IDishRepository;
import com.example.foodplaza.infrastructure.out.jpa.repository.IOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.*;

import java.util.*;

import java.util.Optional;

 class OrderAdapterImplTest {

    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private IDishRepository dishRepository;

    @Mock
    private IOrderEntityMapper orderEntityMapper;

    private OrderAdapterImpl orderAdapter;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this); // Inicializar los mocks
        orderAdapter = new OrderAdapterImpl(orderRepository, dishRepository, orderEntityMapper);
    }


     @Test
    void testGetOrderById_withExistingOrder_shouldReturnOrder() {
        // Arrange
        Long orderId = 1L;
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setIdOrder(orderId);

        when(orderRepository.findByIdWithDishes(orderId)).thenReturn(Optional.of(orderEntity));

        // Act
        Optional<OrderModel> result = orderAdapter.getOrderById(orderId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(orderId, result.get().getIdOrder());
    }

    @Test
    void testValidateDishesBelongToRestaurant_withValidDishes_shouldReturnTrue() {
        // Arrange
        Long restaurantId = 1L;
        List<Long> dishIds = Arrays.asList(1L, 2L);

        DishEntity dishEntity1 = new DishEntity();
        dishEntity1.setIdDish(1L);
        RestaurantEntity restaurantEntity = new RestaurantEntity();
        restaurantEntity.setIdRestaurant(restaurantId);
        dishEntity1.setIdRestaurant(restaurantEntity);

        DishEntity dishEntity2 = new DishEntity();
        dishEntity2.setIdDish(2L);
        dishEntity2.setIdRestaurant(restaurantEntity);

        List<DishEntity> dishes = Arrays.asList(dishEntity1, dishEntity2);
        when(dishRepository.findAllById(dishIds)).thenReturn(dishes);

        // Act
        boolean result = orderAdapter.validateDishesBelongToRestaurant(restaurantId, dishIds);

        // Assert
        assertTrue(result);
    }

    @Test
     void testValidateDishesBelongToRestaurant_withDishFromDifferentRestaurant_shouldReturnFalse() {
        // Arrange
        Long restaurantId = 1L;
        List<Long> dishIds = Arrays.asList(1L, 2L);

        DishEntity dishEntity1 = new DishEntity();
        dishEntity1.setIdDish(1L);
        RestaurantEntity restaurantEntity1 = new RestaurantEntity();
        restaurantEntity1.setIdRestaurant(restaurantId);
        dishEntity1.setIdRestaurant(restaurantEntity1);

        DishEntity dishEntity2 = new DishEntity();
        dishEntity2.setIdDish(2L);
        RestaurantEntity restaurantEntity2 = new RestaurantEntity();
        restaurantEntity2.setIdRestaurant(2L); // Otro restaurante
        dishEntity2.setIdRestaurant(restaurantEntity2);

        List<DishEntity> dishes = Arrays.asList(dishEntity1, dishEntity2);
        when(dishRepository.findAllById(dishIds)).thenReturn(dishes);

        // Act
        boolean result = orderAdapter.validateDishesBelongToRestaurant(restaurantId, dishIds);

        // Assert
        assertFalse(result);
    }

}
