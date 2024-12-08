package com.example.foodplaza.application.handler.impl;

import com.example.foodplaza.application.dto.request.OrderRequestDto;
import com.example.foodplaza.application.dto.response.OrderResponseDto;
import com.example.foodplaza.application.handler.IOrderHandlerPort;
import com.example.foodplaza.application.mapper.request.IOrderRequestMapper;
import com.example.foodplaza.application.mapper.response.IOrderResponseMapper;
import com.example.foodplaza.domain.api.IOrderServicePort;
import com.example.foodplaza.domain.model.OrderModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class OrderHandlerImpl implements IOrderHandlerPort {

    private final IOrderServicePort orderServicePort;
    private final IOrderRequestMapper orderRequestMapper;
    private final IOrderResponseMapper orderResponseMapper;

    @Override
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        // Convertir el DTO a modelo de dominio
        OrderModel orderModel = orderRequestMapper.toModel(orderRequestDto);

        // Crear el pedido a través del caso de uso
        OrderModel createdOrder = orderServicePort.createOrder(orderModel);

        // Convertir el modelo creado a DTO de respuesta
        return orderResponseMapper.toDto(createdOrder);
    }




    @Override
    public OrderResponseDto markOrderAsReadyAndNotify(Long orderId) {
        // Llamar al caso de uso para marcar como listo y notificar
        OrderModel updatedOrder = orderServicePort.markOrderAsReadyAndNotify(orderId);
        return orderResponseMapper.toDto(updatedOrder);
    }

    @Override
    public OrderResponseDto markOrderAsDelivered(Long orderId, String securityPin) {
        // Llamar al caso de uso para marcar como entregado
        OrderModel updatedOrder = orderServicePort.markOrderAsDelivered(orderId, securityPin);
        return orderResponseMapper.toDto(updatedOrder);
    }


    @Override
    public Page<OrderResponseDto> getOrdersByStateAndRestaurant(String stateOrder, Long idRestaurant, Pageable pageable) {
        return orderServicePort.getOrdersByStateAndRestaurant(stateOrder, idRestaurant, pageable)
                .map(orderResponseMapper::toDto);
    }


    @Override
    public OrderResponseDto getOrderById(Long idOrder) {
        OrderModel orderModel = orderServicePort.getOrderById(idOrder)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        // Convertir el modelo a DTO usando el mapper
        return orderResponseMapper.toDto(orderModel);
    }

    @Override
    public List<OrderResponseDto> getOrdersByChefId(Long chefId) {
        return List.of();
    }


    @Override
    public OrderResponseDto assignEmployeeToOrder(Long orderId, Long employeeId) {
        OrderModel updatedOrder = orderServicePort.assignEmployeeToOrder(orderId, employeeId);
        return orderResponseMapper.toDto(updatedOrder);
    }

}
