package com.example.foodplaza.application.handler;

import com.example.foodplaza.application.dto.request.OrderRequestDto;
import com.example.foodplaza.application.dto.response.OrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderHandlerPort {
    OrderResponseDto createOrder(OrderRequestDto orderRequestDto);
    OrderResponseDto getOrderById(Long idOrder);
    List<OrderResponseDto> getOrdersByChefId(Long chefId);
    OrderResponseDto assignChefToOrder(Long orderId, Long chefId);
    OrderResponseDto markOrderAsReady(Long orderId);
    Page<OrderResponseDto> getOrdersByStateAndRestaurant(String stateOrder, Long idRestaurant, Pageable pageable);

}
