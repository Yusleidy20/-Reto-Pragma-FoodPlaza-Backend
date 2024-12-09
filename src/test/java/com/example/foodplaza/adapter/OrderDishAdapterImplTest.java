package com.example.foodplaza.adapter;

import com.example.foodplaza.domain.model.DishModel;
import com.example.foodplaza.domain.model.OrderDishModel;
import com.example.foodplaza.infrastructure.out.jpa.adapter.OrderDishAdapterImpl;
import com.example.foodplaza.infrastructure.out.jpa.entity.DishEntity;
import com.example.foodplaza.infrastructure.out.jpa.entity.OrderDishEntity;
import com.example.foodplaza.infrastructure.out.jpa.entity.OrderEntity;
import com.example.foodplaza.infrastructure.out.jpa.mapper.IOrderDishEntityMapper;
import com.example.foodplaza.infrastructure.out.jpa.repository.IOrderDishRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderDishAdapterImplTest {

    @Mock
    private IOrderDishRepository orderDishRepository;

    @Mock
    private IOrderDishEntityMapper orderDishEntityMapper;

    @InjectMocks
    private OrderDishAdapterImpl orderDishAdapter;

    private OrderDishModel orderDishModel;
    private List<OrderDishModel> orderDishModels;
    private OrderDishEntity orderDishEntity;

    @BeforeEach
    void setUp() {
        // Inicializando un modelo de plato de pedido para las pruebas
        DishModel dishModel = new DishModel();
        dishModel.setIdDish(1L);

        orderDishModel = new OrderDishModel();
        orderDishModel.setIdOrder(1L);
        orderDishModel.setDish(dishModel);
        orderDishModel.setAmount(2);

        orderDishModels = List.of(orderDishModel);

        orderDishEntity = new OrderDishEntity();
        orderDishEntity.setAmount(2);
        orderDishEntity.setDish(new DishEntity());
        orderDishEntity.getDish().setIdDish(1L);
        orderDishEntity.setOrders(new OrderEntity());
        orderDishEntity.getOrders().setIdOrder(1L);
    }

    /**
     * Verifica que los platos de un pedido se guardan correctamente.
     */
    @Test
    void saveOrderDishes_shouldSaveOrderDishes() {

        // Ejecutando el método a probar
        orderDishAdapter.saveOrderDishes(orderDishModels);

        // Usando ArgumentCaptor para capturar el argumento que se pasa al método saveAll
        ArgumentCaptor<List<OrderDishEntity>> captor = ArgumentCaptor.forClass((Class<List<OrderDishEntity>>) (Class<?>) List.class);

        verify(orderDishRepository).saveAll(captor.capture());

        // Verificando que el argumento capturado es igual a la entidad esperada
        List<OrderDishEntity> capturedEntities = captor.getValue();
        assertNotNull(capturedEntities);
        assertEquals(1, capturedEntities.size());
        OrderDishEntity capturedEntity = capturedEntities.get(0);

        // Comparando las propiedades de las entidades
        assertEquals(orderDishEntity.getAmount(), capturedEntity.getAmount());
        assertEquals(orderDishEntity.getDish().getIdDish(), capturedEntity.getDish().getIdDish());
        assertEquals(orderDishEntity.getOrders().getIdOrder(), capturedEntity.getOrders().getIdOrder());
    }
}

