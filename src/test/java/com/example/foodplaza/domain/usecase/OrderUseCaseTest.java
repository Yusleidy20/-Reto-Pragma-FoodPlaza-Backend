package com.example.foodplaza.domain.usecase;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.foodplaza.domain.api.IValidatorServicePort;

import com.example.foodplaza.domain.model.OrderModel;

import com.example.foodplaza.domain.model.RestaurantModel;
import com.example.foodplaza.domain.spi.feignclients.ISmsClientPort;
import com.example.foodplaza.domain.spi.persistence.IOrderDishPersistencePort;
import com.example.foodplaza.domain.spi.persistence.IOrderPersistencePort;

import com.example.foodplaza.infrastructure.configuration.Constants;
import com.example.foodplaza.infrastructure.out.jpa.feignclients.UserDto;
import com.example.foodplaza.infrastructure.out.jpa.feignclients.mapper.ITraceabilityFeignClient;
import com.example.foodplaza.infrastructure.out.jpa.feignclients.mapper.IUserFeignClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

class OrderUseCaseTest {

    @Mock
    private IOrderPersistencePort orderPersistencePort;

    @Mock
    private IOrderDishPersistencePort orderDishPersistencePort;

    @Mock
    private IValidatorServicePort validatorServicePort;

    @Mock
    private IUserFeignClient userClientPort;

    @Mock
    private ISmsClientPort smsClientPort;

    @Mock
    private ITraceabilityFeignClient traceabilityFeignClient;

    @InjectMocks
    private OrderUseCase orderUseCase;

    private OrderModel orderModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        orderModel = new OrderModel();
        orderModel.setIdOrder(1L);
        orderModel.setCustomerId(100L);
        orderModel.setStateOrder(Constants.PENDING);
        orderModel.setRestaurant(new RestaurantModel(1L));
        orderModel.setDateOrder(LocalDate.now());

        // Configurar la autenticación mockeada
        Authentication authentication = mock(Authentication.class);
        when(authentication.getDetails()).thenReturn(1L);  // Suponiendo que el customerId es 1L
        when(userClientPort.getUserById(anyLong())).thenReturn(new UserDto(1L, "John", "Doe", 123456789L, "123456789"));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void testCreateOrder() {
        // Este test verifica que al crear una orden, se guarde correctamente con el estado "PENDING".
        // Arrange: Preparamos el comportamiento simulado de la persistencia para que guarde el pedido.
        when(orderPersistencePort.saveOrder(any(OrderModel.class))).thenReturn(orderModel);

        // Act: Llamamos al método que queremos probar, en este caso, crear un pedido.
        OrderModel createdOrder = orderUseCase.createOrder(orderModel);

        // Assert: Comprobamos que el pedido creado no sea nulo, que su estado sea 'PENDING' y que la persistencia haya sido llamada una vez.
        assertNotNull(createdOrder);
        assertEquals(Constants.PENDING, createdOrder.getStateOrder());
        verify(orderPersistencePort, times(1)).saveOrder(any(OrderModel.class));
    }

    @Test
    void testGetOrderById() {
        // Este test verifica que al obtener un pedido por su ID, se retorne el pedido correcto.
        // Arrange: Preparamos el comportamiento simulado para obtener un pedido por su ID.
        Long orderId = 1L;
        when(orderPersistencePort.getOrderById(orderId)).thenReturn(Optional.of(orderModel));

        // Act: Llamamos al método que queremos probar, en este caso, obtener un pedido por su ID.
        Optional<OrderModel> result = orderUseCase.getOrderById(orderId);

        // Assert: Comprobamos que el resultado no sea vacío y que el pedido obtenido sea el esperado.
        assertTrue(result.isPresent());
        assertEquals(orderModel, result.get());
        verify(orderPersistencePort, times(1)).getOrderById(orderId);
    }

    @Test
    void testGetOrdersByChefId() {
        // Este test verifica que al obtener los pedidos asignados a un chef, se retornen los pedidos correspondientes.
        // Arrange: Preparamos el comportamiento simulado para obtener los pedidos de un chef específico.
        Long chefId = 101L;
        List<OrderModel> orders = List.of(orderModel);
        when(orderPersistencePort.getOrdersByChefId(chefId)).thenReturn(orders);

        // Act: Llamamos al método que queremos probar, en este caso, obtener los pedidos por chef ID.
        List<OrderModel> result = orderUseCase.getOrdersByChefId(chefId);

        // Assert: Comprobamos que la lista de pedidos obtenida sea la misma que la esperada.
        assertEquals(orders, result);
        verify(orderPersistencePort, times(1)).getOrdersByChefId(chefId);
    }

    @Test
    void testAssignEmployeeToOrder() {
        // Este test verifica que al asignar un empleado a un pedido, el pedido se actualice correctamente con el empleado asignado.
        // Arrange: Preparamos el comportamiento simulado para obtener el pedido y el empleado, y para guardar el pedido actualizado.
        Long orderId = 1L;
        Long employeeId = 10L;
        UserDto employee = new UserDto();
        employee.setUserId(employeeId);
        employee.setEmail("employee@example.com");

        when(orderPersistencePort.getOrderById(orderId)).thenReturn(Optional.of(orderModel));
        when(userClientPort.getUserById(employeeId)).thenReturn(employee);
        when(orderPersistencePort.saveOrder(any(OrderModel.class))).thenReturn(orderModel);

        // Act: Llamamos al método que queremos probar, en este caso, asignar un empleado al pedido.
        OrderModel assignedOrder = orderUseCase.assignEmployeeToOrder(orderId, employeeId);

        // Assert: Comprobamos que el pedido tenga asignado al empleado y que su estado haya cambiado a "IN_PREPARATION".
        assertEquals(employeeId, assignedOrder.getChefId());
        assertEquals(Constants.IN_PREPARATION, assignedOrder.getStateOrder());
        verify(orderPersistencePort, times(1)).saveOrder(any(OrderModel.class));
    }

    @Test
    void testCancelOrder() {
        // Este test verifica que al cancelar un pedido, su estado se actualice a "CANCELED".
        // Arrange: Preparamos el comportamiento simulado para obtener el pedido y guardar el pedido actualizado con el estado "CANCELED".
        Long customerId = 100L;
        orderModel.setCustomerId(customerId);
        when(orderPersistencePort.getOrderById(1L)).thenReturn(Optional.of(orderModel));
        when(orderPersistencePort.saveOrder(any(OrderModel.class))).thenReturn(orderModel);

        // Act: Llamamos al método que queremos probar, en este caso, cancelar un pedido.
        OrderModel canceledOrder = orderUseCase.cancelOrder(1L, customerId);

        // Assert: Comprobamos que el estado del pedido haya cambiado a "CANCELED".
        assertEquals(Constants.CANCELED, canceledOrder.getStateOrder());
        verify(orderPersistencePort, times(1)).saveOrder(any(OrderModel.class));
    }

    @Test
    void testCancelOrderThrowsExceptionWhenCustomerIdDoesNotMatch() {
        // Este test verifica que se lance una excepción si un cliente intenta cancelar un pedido que no le pertenece.
        // Arrange: Preparamos un pedido con un customerId diferente al proporcionado para la cancelación.
        Long customerId = 100L;
        orderModel.setCustomerId(200L); // Mismatched customerId
        when(orderPersistencePort.getOrderById(1L)).thenReturn(Optional.of(orderModel));

        // Act & Assert: Verificamos que se lance una excepción IllegalStateException con el mensaje esperado.
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            orderUseCase.cancelOrder(1L, customerId);
        });
        assertEquals("You can only cancel your own orders.", exception.getMessage());
    }

    @Test
    void testGetOrdersByStateAndRestaurant() {
        // Este test verifica que al obtener los pedidos filtrados por estado y restaurante, se retornen los pedidos correspondientes.
        // Arrange: Preparamos el comportamiento simulado para obtener los pedidos filtrados por estado y restaurante.
        String stateOrder = Constants.PENDING;
        Long restaurantId = 1L;
        Pageable pageable = Pageable.unpaged();
        Page<OrderModel> orders = Page.empty();
        when(orderPersistencePort.getOrdersByStateAndRestaurant(stateOrder, restaurantId, pageable)).thenReturn(orders);

        // Act: Llamamos al método que queremos probar, en este caso, obtener los pedidos filtrados por estado y restaurante.
        Page<OrderModel> result = orderUseCase.getOrdersByStateAndRestaurant(stateOrder, restaurantId, pageable);

        // Assert: Comprobamos que la página de pedidos obtenida sea la misma que la esperada.
        assertEquals(orders, result);
        verify(orderPersistencePort, times(1)).getOrdersByStateAndRestaurant(stateOrder, restaurantId, pageable);
    }
}
