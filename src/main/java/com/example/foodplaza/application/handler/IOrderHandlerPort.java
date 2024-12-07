package com.example.foodplaza.application.handler;

import com.example.foodplaza.application.dto.request.OrderRequestDto;
import com.example.foodplaza.application.dto.response.OrderResponseDto;

import java.util.List;

public interface IOrderHandlerPort {
    OrderResponseDto createOrder(OrderRequestDto orderRequestDto);
    OrderResponseDto getOrderById(Long idOrder);
    List<OrderResponseDto> getOrdersByCustomerId(Long customerId);
    OrderResponseDto assignChefToOrder(Long orderId, Long chefId);
    OrderResponseDto markOrderAsReady(Long orderId);
}
