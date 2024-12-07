package com.example.foodplaza.domain.usecase;


import com.example.foodplaza.domain.api.IOrderServicePort;
import com.example.foodplaza.domain.api.IValidatorServicePort;
import com.example.foodplaza.domain.model.OrderModel;
import com.example.foodplaza.domain.spi.persistence.IOrderDishPersistencePort;
import com.example.foodplaza.domain.spi.persistence.IOrderPersistencePort;
import com.example.foodplaza.infrastructure.configuration.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
        // Verificar que el pedido existe
        OrderModel order = orderPersistencePort.getOrderById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        // Validar que el pedido esté en estado "PENDIENTE"
        if (!Constants.PENDING.equals(order.getStateOrder())) {
            throw new IllegalStateException("Only orders in 'PENDING' state can be assigned.");
        }

        // Asignar el empleado y cambiar el estado del pedido
        order.setChefId(employeeId);
        order.setStateOrder(Constants.IN_PREPARATION);

        // Guardar los cambios
        return orderPersistencePort.saveOrder(order);
    }

}
