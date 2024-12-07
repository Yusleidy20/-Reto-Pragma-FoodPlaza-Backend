package com.example.foodplaza.domain.api;

import com.example.foodplaza.domain.model.OrderModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IOrderServicePort {
    OrderModel createOrder(OrderModel orderModel);
    Optional<OrderModel> getOrderById(Long idOrder);
    List<OrderModel> getOrdersByChefId(Long chefId);
    OrderModel assignChefToOrder(Long orderId, Long chefId); // Nuevo método
    OrderModel markOrderAsReady(Long orderId); // Nuevo método
    Page<OrderModel> getOrdersByStateAndRestaurant(String stateOrder, Long idRestaurant, Pageable pageable);

}
