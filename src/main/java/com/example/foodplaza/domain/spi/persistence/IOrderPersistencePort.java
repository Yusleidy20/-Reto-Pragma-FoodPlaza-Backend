package com.example.foodplaza.domain.spi.persistence;

import com.example.foodplaza.domain.model.OrderModel;

import java.util.List;
import java.util.Optional;

public interface IOrderPersistencePort {
    OrderModel saveOrder(OrderModel orderModel);
    Optional<OrderModel> getOrderById(Long idOrder);
    List<OrderModel> getOrdersByCustomerId(Long customerId);
    boolean validateDishesBelongToRestaurant(Long restaurantId, List<Long> dishIds);
    boolean hasActiveOrders(Long customerId);
}
