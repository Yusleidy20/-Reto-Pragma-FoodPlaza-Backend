package com.example.foodplaza.domain.usecase;

import com.example.foodplaza.domain.model.DishModel;
import com.example.foodplaza.domain.model.OrderDishModel;
import com.example.foodplaza.domain.model.OrderModel;
import com.example.foodplaza.domain.model.RestaurantModel;
import com.example.foodplaza.domain.spi.persistence.IOrderPersistencePort;
import com.example.foodplaza.infrastructure.configuration.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidatorServiceTest {

    @Mock
    private IOrderPersistencePort orderPersistencePort;

    @InjectMocks
    private ValidatorService validatorService;

    private OrderModel orderModel;
    private OrderDishModel orderDishModel;
    private RestaurantModel restaurantModel;

    @BeforeEach
    void setUp() {
        // Inicializando un pedido con datos de prueba
        restaurantModel = new RestaurantModel();
        restaurantModel.setIdRestaurant(1L);

        orderDishModel = new OrderDishModel();
        orderDishModel.setAmount(2);
        DishModel dishModel = new DishModel();
        dishModel.setIdDish(1L);
        orderDishModel.setDish(dishModel);

        orderModel = new OrderModel();
        orderModel.setChefId(1L);
        orderModel.setRestaurant(restaurantModel);
        orderModel.setOrderDishes(List.of(orderDishModel));
        orderModel.setStateOrder(Constants.PENDING);
    }

    /**
     * Verifica que la validación de un pedido se realiza correctamente.
     */
    @Test
    void validateOrder_shouldValidateSuccessfully() {
        // Configuración del mock para obtener un pedido activo por chef
        when(orderPersistencePort.getOrdersByChefId(orderModel.getChefId())).thenReturn(List.of());

        // Configuración del mock para validar que los platos pertenecen al restaurante
        when(orderPersistencePort.validateDishesBelongToRestaurant(orderModel.getRestaurant().getIdRestaurant(),
                List.of(orderDishModel.getDish().getIdDish()))).thenReturn(true);

        // Ejecutando el método a probar
        validatorService.validateOrder(orderModel);

        // Verificando las interacciones de los mocks
        verify(orderPersistencePort).getOrdersByChefId(orderModel.getChefId());
        verify(orderPersistencePort).validateDishesBelongToRestaurant(orderModel.getRestaurant().getIdRestaurant(),
                List.of(orderDishModel.getDish().getIdDish()));
    }

    /**
     * Verifica que se lanza una excepción cuando el chef tiene un pedido activo.
     */
    @Test
    void validateOrder_shouldThrowException_whenChefHasActiveOrder() {
        // Configuración del mock para obtener un pedido activo por chef
        OrderModel activeOrder = new OrderModel();
        activeOrder.setStateOrder(Constants.PENDING);
        when(orderPersistencePort.getOrdersByChefId(orderModel.getChefId())).thenReturn(List.of(activeOrder));

        // Ejecutando el método y verificando que se lanza la excepción
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            validatorService.validateOrder(orderModel);
        });

        assertEquals("The customer already has an active order.", exception.getMessage());
        verify(orderPersistencePort).getOrdersByChefId(orderModel.getChefId());
    }

    /**
     * Verifica que se lanza una excepción cuando el pedido no tiene platos.
     */
    @Test
    void validateOrderDishes_shouldThrowException_whenOrderHasNoDishes() {
        // Configuración de un pedido vacío
        orderModel.setOrderDishes(new ArrayList<>());

        // Ejecutando el método y verificando que se lanza la excepción
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            validatorService.validateOrder(orderModel);
        });

        assertEquals("The order must contain at least one dish.", exception.getMessage());
    }

    /**
     * Verifica que se lanza una excepción cuando un plato tiene cantidad inválida.
     */
    @Test
    void validateOrderDishes_shouldThrowException_whenDishHasInvalidAmount() {
        // Configuración del plato con cantidad inválida
        orderDishModel.setAmount(-1);

        // Ejecutando el método y verificando que se lanza la excepción
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            validatorService.validateOrder(orderModel);
        });

        assertEquals("Each dish must have a positive quantity.", exception.getMessage());
    }

    /**
     * Verifica que se lanza una excepción cuando un plato no pertenece al restaurante especificado.
     */
    @Test
    void validateRestaurantAndDishes_shouldThrowException_whenDishDoesNotBelongToRestaurant() {
        // Configuración del mock para validar que los platos no pertenecen al restaurante
        when(orderPersistencePort.validateDishesBelongToRestaurant(orderModel.getRestaurant().getIdRestaurant(),
                List.of(orderDishModel.getDish().getIdDish()))).thenReturn(false);

        // Ejecutando el método y verificando que se lanza la excepción
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            validatorService.validateOrder(orderModel);
        });

        assertEquals("All dishes must belong to the specified restaurant.", exception.getMessage());
    }

    /**
     * Verifica que la validación de estado de un pedido pendiente se realiza correctamente.
     */
    @Test
    void validatePendingOrder_shouldValidateSuccessfully_whenOrderIsPending() {
        // Ejecutando el método a probar
        validatorService.validatePendingOrder(orderModel);

        // Verificando que no se lanza excepción
    }

    /**
     * Verifica que se lanza una excepción si el pedido no está pendiente.
     */
    @Test
    void validatePendingOrder_shouldThrowException_whenOrderIsNotPending() {
        // Cambiando el estado del pedido
        orderModel.setStateOrder(Constants.IN_PREPARATION);

        // Ejecutando el método y verificando que se lanza la excepción
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            validatorService.validatePendingOrder(orderModel);
        });

        assertEquals("Only pending orders can be updated.", exception.getMessage());
    }
}
