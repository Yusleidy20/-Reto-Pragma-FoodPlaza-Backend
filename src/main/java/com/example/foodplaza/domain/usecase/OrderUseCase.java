package com.example.foodplaza.domain.usecase;


import com.example.foodplaza.domain.api.IOrderServicePort;
import com.example.foodplaza.domain.api.IValidatorServicePort;
import com.example.foodplaza.domain.model.OrderModel;
import com.example.foodplaza.domain.spi.persistence.IOrderDishPersistencePort;
import com.example.foodplaza.domain.spi.persistence.IOrderPersistencePort;
import com.example.foodplaza.infrastructure.configuration.Constants;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IOrderDishPersistencePort orderDishPersistencePort;
    private final IValidatorServicePort validatorServicePort;

    @Override
    public OrderModel createOrder(OrderModel orderModel) {
        if (orderModel.getRestaurant() == null || orderModel.getRestaurant().getIdRestaurant() == null) {
            throw new IllegalArgumentException("Restaurant ID is required.");
        }

        // Verificar si el cliente tiene pedidos en proceso
        boolean hasActiveOrders = orderPersistencePort.hasActiveOrders(orderModel.getCustomerId());
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
    public List<OrderModel> getOrdersByCustomerId(Long customerId) {
        return orderPersistencePort.getOrdersByCustomerId(customerId);
    }
    @Override
    public OrderModel assignChefToOrder(Long orderId, Long chefId) {
        // Obtener el pedido
        OrderModel order = orderPersistencePort.getOrderById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        // Validar que el estado sea "Pendiente"
        validatorServicePort.validatePendingOrder(order);

        // Asignar el chef y cambiar el estado
        order.setChefId(chefId);
        order.setStateOrder(Constants.IN_PREPARATION);

        // Guardar el pedido actualizado
        return orderPersistencePort.saveOrder(order);
    }

    @Override
    public OrderModel markOrderAsReady(Long orderId) {
        // Obtener el pedido
        OrderModel order = orderPersistencePort.getOrderById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        // Validar que el estado sea "En preparación"
        if (!Constants.IN_PREPARATION.equals(order.getStateOrder())) {
            throw new IllegalStateException("Only orders in preparation can be marked as ready.");
        }

        // Cambiar el estado a "Listo"
        order.setStateOrder(Constants.READY);

        // Guardar el pedido actualizado
        return orderPersistencePort.saveOrder(order);
    }
}
