package com.example.foodplaza.domain.usecase;


import com.example.foodplaza.domain.api.IOrderServicePort;
import com.example.foodplaza.domain.api.IValidatorServicePort;
import com.example.foodplaza.domain.model.OrderModel;
import com.example.foodplaza.domain.spi.feignclients.ISmsClientPort;
import com.example.foodplaza.domain.spi.persistence.IOrderDishPersistencePort;
import com.example.foodplaza.domain.spi.persistence.IOrderPersistencePort;
import com.example.foodplaza.infrastructure.configuration.Constants;
import com.example.foodplaza.infrastructure.out.jpa.feignclients.UserDto;
import com.example.foodplaza.infrastructure.out.jpa.feignclients.mapper.ISmsFeignClient;
import com.example.foodplaza.infrastructure.out.jpa.feignclients.mapper.IUserFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IOrderDishPersistencePort orderDishPersistencePort;
    private final IValidatorServicePort validatorServicePort;
    private final IUserFeignClient userClientPort;
    private final ISmsClientPort smsClientPort;

    @Override
    public OrderModel createOrder(OrderModel orderModel) {
        if (orderModel.getRestaurant() == null || orderModel.getRestaurant().getIdRestaurant() == null) {
            throw new IllegalArgumentException("Restaurant ID is required.");
        }

        // Validar que el pedido no esté en estado "Entregado"
        if (Constants.DELIVERED.equals(orderModel.getStateOrder())) {
            throw new IllegalStateException("Delivered orders cannot be modified.");
        }

        // Obtener el userId desde el contexto de seguridad (de la autenticación)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long customerId = (Long) authentication.getDetails();  // Obtiene el userId del token

        // Asignar el customerId al modelo
        orderModel.setCustomerId(customerId);

        // Verificar si el cliente tiene pedidos en proceso
        boolean hasActiveOrders = orderPersistencePort.hasActiveOrders(orderModel.getChefId());
        if (hasActiveOrders) {
            throw new IllegalStateException("The customer already has an active order in process.");
        }

        // Configurar estado inicial y fecha
        orderModel.setStateOrder(Constants.PENDING);
        orderModel.setDateOrder(LocalDate.now());

        return orderPersistencePort.saveOrder(orderModel);
    }




    @Override
    public Optional<OrderModel> getOrderById(Long idOrder) {
        return orderPersistencePort.getOrderById(idOrder);
    }

    @Override
    public List<OrderModel> getOrdersByChefId(Long chefId) {
        return orderPersistencePort.getOrdersByChefId(chefId);
    }

    public Page<OrderModel> getOrdersByStateAndRestaurant(String stateOrder, Long idRestaurant, Pageable pageable) {
        if (stateOrder == null || stateOrder.isBlank()) {
            throw new IllegalArgumentException("State order is required.");
        }

        if (idRestaurant == null) {
            throw new IllegalArgumentException("Restaurant ID is required.");
        }

        return orderPersistencePort.getOrdersByStateAndRestaurant(stateOrder, idRestaurant, pageable);
    }

    @Override
    public OrderModel assignEmployeeToOrder(Long orderId, Long employeeId) {
        // Obtener el pedido con todas las relaciones
        OrderModel order = orderPersistencePort.getOrderById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        // Validar que el pedido no esté en estado "Entregado"
        if (Constants.DELIVERED.equals(order.getStateOrder())) {
            throw new IllegalStateException("Delivered orders cannot be modified.");
        }

        // Validar que el pedido esté en estado "PENDIENTE"
        if (!Constants.PENDING.equals(order.getStateOrder())) {
            throw new IllegalStateException("Only orders in 'PENDING' state can be assigned.");
        }

        // Asignar el empleado y cambiar el estado del pedido
        order.setChefId(employeeId);
        order.setStateOrder(Constants.IN_PREPARATION);

        // Guardar el pedido y sus relaciones
        return orderPersistencePort.saveOrder(order);
    }



    @Override
    public OrderModel markOrderAsReadyAndNotify(Long orderId) {
        // Obtener el pedido
        OrderModel order = orderPersistencePort.getOrderById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        // Validar que el pedido no esté en estado "Entregado"
        if (Constants.DELIVERED.equals(order.getStateOrder())) {
            throw new IllegalStateException("Delivered orders cannot be modified.");
        }

        // Validar que el pedido esté en estado "EN_PREPARACIÓN"
        if (!Constants.IN_PREPARATION.equals(order.getStateOrder())) {
            throw new IllegalStateException("Only orders in 'IN_PREPARATION' state can be marked as ready.");
        }

        // Generar PIN y asignarlo al pedido
        String pin = generatePin();
        order.setSecurityPin(pin);

        // Cambiar el estado a "Listo"
        order.setStateOrder(Constants.READY);

        // Guardar el pedido con el PIN generado
        OrderModel updatedOrder = orderPersistencePort.saveOrder(order);

        // Obtener información del cliente desde el microservicio Usuario
        UserDto customer = userClientPort.getUserById(order.getCustomerId());
        if (customer == null || customer.getPhoneNumber() == null) {
            throw new IllegalStateException("Customer information is missing.");
        }

        // Enviar notificación SMS con el PIN
        String message = String.format("Your order is ready! Please use PIN: %s to collect it.", pin);
        smsClientPort.sendSms(customer.getPhoneNumber(), message);

        return updatedOrder;
    }


    private String generatePin() {
        return String.valueOf((int) (Math.random() * 9000) + 1000); // PIN de 4 dígitos
    }


    @Override
    public OrderModel markOrderAsDelivered(Long orderId, String providedPin) {
        // Obtener el pedido por ID
        OrderModel order = orderPersistencePort.getOrderById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        // Validar que el estado actual sea "Listo"
        if (!Constants.READY.equals(order.getStateOrder())) {
            throw new IllegalStateException("Only orders in 'READY' state can be marked as delivered.");
        }

        // Validar el PIN de seguridad
        if (!order.getSecurityPin().equals(providedPin)) {
            throw new IllegalStateException("Invalid security PIN.");
        }

        // Cambiar el estado a "Entregado"
        order.setStateOrder(Constants.DELIVERED);

        // Guardar los cambios
        return orderPersistencePort.saveOrder(order);
    }


}
