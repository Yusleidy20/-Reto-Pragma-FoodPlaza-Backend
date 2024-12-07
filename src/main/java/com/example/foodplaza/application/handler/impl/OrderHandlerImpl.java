package com.example.foodplaza.application.handler.impl;

import com.example.foodplaza.application.dto.request.OrderRequestDto;
import com.example.foodplaza.application.dto.response.OrderResponseDto;
import com.example.foodplaza.application.handler.IOrderHandlerPort;
import com.example.foodplaza.application.mapper.request.IOrderRequestMapper;
import com.example.foodplaza.application.mapper.response.IOrderResponseMapper;
import com.example.foodplaza.domain.api.IOrderServicePort;
import com.example.foodplaza.domain.model.OrderModel;
import lombok.RequiredArgsConstructor;
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
    public OrderResponseDto assignChefToOrder(Long orderId, Long chefId) {
        // Asignar el chef y devolver el pedido actualizado
        OrderModel updatedOrder = orderServicePort.assignChefToOrder(orderId, chefId);
        return orderResponseMapper.toDto(updatedOrder);
    }

    @Override
    public OrderResponseDto markOrderAsReady(Long orderId) {
        // Marcar el pedido como listo y devolverlo
        OrderModel updatedOrder = orderServicePort.markOrderAsReady(orderId);
        return orderResponseMapper.toDto(updatedOrder);
    }

    @Override
    public OrderResponseDto getOrderById(Long idOrder) {
        OrderModel orderModel = orderServicePort.getOrderById(idOrder)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        // Convertir el modelo a DTO usando el mapper
        return orderResponseMapper.toDto(orderModel);
    }


    @Override
    public List<OrderResponseDto> getOrdersByCustomerId(Long customerId) {
        // Obtener los pedidos del cliente
        return orderServicePort.getOrdersByCustomerId(customerId).stream()
                .map(orderResponseMapper::toDto)
                .toList();
    }
}
