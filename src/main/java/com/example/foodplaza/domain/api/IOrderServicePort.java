package com.example.foodplaza.domain.api;

import com.example.foodplaza.domain.model.OrderModel;

import java.util.List;
import java.util.Optional;

public interface IOrderServicePort {
    OrderModel createOrder(OrderModel orderModel);
    Optional<OrderModel> getOrderById(Long idOrder);
    List<OrderModel> getOrdersByCustomerId(Long customerId);
    OrderModel assignChefToOrder(Long orderId, Long chefId); // Nuevo método
    OrderModel markOrderAsReady(Long orderId); // Nuevo método
}
