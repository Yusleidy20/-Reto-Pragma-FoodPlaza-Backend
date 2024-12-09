package com.example.foodplaza.domain.spi.persistence;

import com.example.foodplaza.domain.model.OrderModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IOrderPersistencePort {
    OrderModel saveOrder(OrderModel orderModel);
    Optional<OrderModel> getOrderById(Long idOrder);
    List<OrderModel> getOrdersByChefId(Long chefId);
    boolean validateDishesBelongToRestaurant(Long restaurantId, List<Long> dishIds);
    boolean hasActiveOrders(Long chefId);
    Page<OrderModel> getOrdersByStateAndRestaurant(String stateOrder, Long idRestaurant, Pageable pageable);


}
