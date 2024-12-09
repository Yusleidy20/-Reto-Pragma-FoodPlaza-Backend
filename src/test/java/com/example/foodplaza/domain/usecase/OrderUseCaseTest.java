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
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.Optional;

public class OrderUseCaseTest {

    @Mock private IOrderPersistencePort orderPersistencePort;
    @Mock private IOrderDishPersistencePort orderDishPersistencePort;
    @Mock private IValidatorServicePort validatorServicePort;
    @Mock private IUserFeignClient userClientPort;
    @Mock private ISmsClientPort smsClientPort;
    @Mock private ITraceabilityFeignClient traceabilityFeignClient;
    @Mock private SecurityContext securityContext;
    @Mock private Authentication authentication;

    private OrderUseCase orderUseCase;

    private Long customerId = 1L;
    private Long employeeId = 2L;
    private Long orderId = 1L;
    private OrderModel orderModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderUseCase = new OrderUseCase(orderPersistencePort, orderDishPersistencePort,
                validatorServicePort, userClientPort, smsClientPort,
                traceabilityFeignClient);

        // Configuración de la autenticación simulada
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getDetails()).thenReturn(customerId); // Simulamos que el usuario autenticado es el cliente

        SecurityContextHolder.setContext(securityContext);

        // Creamos un modelo de orden de ejemplo
        orderModel = new OrderModel();
        orderModel.setIdOrder(orderId);
        orderModel.setCustomerId(customerId);
        orderModel.setStateOrder(Constants.PENDING);
        orderModel.setDateOrder(LocalDate.now());
    }

    @Test
    void testCreateOrder() {
        // Simula la validación de la orden
        doNothing().when(validatorServicePort).validateOrder(orderModel);


        // Simula la creación de la orden
        when(orderPersistencePort.saveOrder(any(OrderModel.class))).thenReturn(orderModel);

        // Llamamos al método del caso de uso
        OrderModel createdOrder = orderUseCase.createOrder(orderModel);

        // Verificamos el resultado
        assertNotNull(createdOrder);
        assertEquals(orderModel.getStateOrder(), createdOrder.getStateOrder());
        assertEquals(customerId, createdOrder.getCustomerId());

        // Verificamos las interacciones con los mocks
        verify(orderPersistencePort).saveOrder(any(OrderModel.class));
        verify(validatorServicePort).validateOrder(orderModel);
    }

    @Test
    void testAssignEmployeeToOrder() {
        // Simulamos la obtención de la orden y la validación de la misma
        when(orderPersistencePort.getOrderById(orderId)).thenReturn(Optional.of(orderModel));

        // Simula la obtención del empleado
        UserDto employee = new UserDto(employeeId, "Employee", "Lastname", 123456L, "employee@example.com");
        when(userClientPort.getUserById(employeeId)).thenReturn(employee);

        // Simula la actualización de la orden
        when(orderPersistencePort.saveOrder(any(OrderModel.class))).thenReturn(orderModel);

        // Llamamos al método
        OrderModel updatedOrder = orderUseCase.assignEmployeeToOrder(orderId, employeeId);

        // Verificamos que la orden fue actualizada
        assertNotNull(updatedOrder);
        assertEquals(employeeId, updatedOrder.getChefId());

        // Verificamos las interacciones con los mocks
        verify(orderPersistencePort).saveOrder(any(OrderModel.class));
        verify(userClientPort).getUserById(employeeId);
    }

    @Test
    void testMarkOrderAsReadyAndNotify() {
        // Simula la obtención de la orden
        when(orderPersistencePort.getOrderById(orderId)).thenReturn(Optional.of(orderModel));

        // Simula la obtención del empleado
        UserDto employee = new UserDto(employeeId, "Employee", "Lastname", 123456L, "employee@example.com");
        when(userClientPort.getUserById(employeeId)).thenReturn(employee);

        // Simula el envío de SMS
        doNothing().when(smsClientPort).sendSms(anyString(), anyString());

        // Simula la actualización de la orden
        when(orderPersistencePort.saveOrder(any(OrderModel.class))).thenReturn(orderModel);

        // Llamamos al método
        OrderModel updatedOrder = orderUseCase.markOrderAsReadyAndNotify(orderId);

        // Verificamos que la orden fue actualizada
        assertNotNull(updatedOrder);
        assertEquals(Constants.READY, updatedOrder.getStateOrder());
        assertNotNull(updatedOrder.getSecurityPin());

        // Verificamos las interacciones con los mocks
        verify(orderPersistencePort).saveOrder(any(OrderModel.class));
        verify(userClientPort).getUserById(employeeId);
        verify(smsClientPort).sendSms(anyString(), anyString());
    }

    @Test
    void testMarkOrderAsDelivered() {
        // Simula la obtención de la orden
        when(orderPersistencePort.getOrderById(orderId)).thenReturn(Optional.of(orderModel));

        // Simula la obtención del empleado
        UserDto employee = new UserDto(employeeId, "Employee", "Lastname", 123456L, "employee@example.com");
        when(userClientPort.getUserById(employeeId)).thenReturn(employee);

        // Simula la actualización de la orden
        when(orderPersistencePort.saveOrder(any(OrderModel.class))).thenReturn(orderModel);

        // Llamamos al método
        OrderModel updatedOrder = orderUseCase.markOrderAsDelivered(orderId, "1234");

        // Verificamos que la orden fue actualizada
        assertNotNull(updatedOrder);
        assertEquals(Constants.DELIVERED, updatedOrder.getStateOrder());

        // Verificamos las interacciones con los mocks
        verify(orderPersistencePort).saveOrder(any(OrderModel.class));
        verify(userClientPort).getUserById(employeeId);
    }

    @Test
    void testCancelOrder() {
        // Simula la obtención de la orden
        when(orderPersistencePort.getOrderById(orderId)).thenReturn(Optional.of(orderModel));

        // Simula la validación de la orden
        doNothing().when(validatorServicePort).validateOrder(orderModel);

        // Simula la actualización de la orden
        when(orderPersistencePort.saveOrder(any(OrderModel.class))).thenReturn(orderModel);

        // Llamamos al método
        OrderModel canceledOrder = orderUseCase.cancelOrder(orderId, customerId);

        // Verificamos que la orden fue cancelada
        assertNotNull(canceledOrder);
        assertEquals(Constants.CANCELED, canceledOrder.getStateOrder());

        // Verificamos las interacciones con los mocks
        verify(orderPersistencePort).saveOrder(any(OrderModel.class));
        verify(validatorServicePort).validatePendingOrder(orderModel);
    }
}
