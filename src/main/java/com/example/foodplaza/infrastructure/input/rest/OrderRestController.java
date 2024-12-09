package com.example.foodplaza.infrastructure.input.rest;

import com.example.foodplaza.application.dto.request.OrderRequestDto;
import com.example.foodplaza.application.dto.request.RestaurantRequestDto;
import com.example.foodplaza.application.dto.response.OrderResponseDto;
import com.example.foodplaza.application.handler.IOrderHandlerPort;
import io.jsonwebtoken.Jwt;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(path = "/user-micro/foodplaza")
@RequiredArgsConstructor
public class OrderRestController {

    private final IOrderHandlerPort orderHandler;

    @PostMapping("/orders")
    @PreAuthorize("hasAuthority('Customer')")
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        OrderResponseDto createdOrder = orderHandler.createOrder(orderRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @GetMapping("/orders")
    @PreAuthorize("hasAuthority('Employee')")
    public ResponseEntity<Page<OrderResponseDto>> getOrdersByStateAndRestaurant(
            @RequestParam String stateOrder,
            @RequestParam Long idRestaurant,
            @RequestParam int page,
            @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderResponseDto> orders = orderHandler.getOrdersByStateAndRestaurant(stateOrder, idRestaurant, pageable);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/orders/{orderId}/assign")
    @PreAuthorize("hasAuthority('Employee')")
    public ResponseEntity<OrderResponseDto> assignEmployeeToOrder(@PathVariable Long orderId) {
        // Obtener el ID del empleado desde los detalles de autenticación
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long employeeId = (Long) authentication.getDetails(); // Extraer el userId

        // Asignar el empleado al pedido
        OrderResponseDto updatedOrder = orderHandler.assignEmployeeToOrder(orderId, employeeId);
        return ResponseEntity.ok(updatedOrder);
    }


    @PutMapping("/orders/{orderId}/ready")
    @PreAuthorize("hasAuthority('Employee')")
    public ResponseEntity<OrderResponseDto> markOrderAsReadyAndNotify(@PathVariable Long orderId) {
        // Cambiar el estado del pedido y notificar al cliente
        OrderResponseDto updatedOrder = orderHandler.markOrderAsReadyAndNotify(orderId);
        return ResponseEntity.ok(updatedOrder);
    }

    @PutMapping("/orders/{orderId}/deliver")
    @PreAuthorize("hasAuthority('Employee')")
    public ResponseEntity<OrderResponseDto> markOrderAsDelivered(
            @PathVariable Long orderId,
            @RequestParam String securityPin) {
        // Llamar al handler para marcar el pedido como entregado
        OrderResponseDto updatedOrder = orderHandler.markOrderAsDelivered(orderId, securityPin);
        return ResponseEntity.ok(updatedOrder);
    }
    @PutMapping("/orders/{orderId}/cancel")
    @PreAuthorize("hasAuthority('Customer')")
    public ResponseEntity<OrderResponseDto> cancelOrder(
            @PathVariable Long orderId,
            Principal principal) {
        // Recuperar el ID del usuario desde el contexto de seguridad
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long customerId = (Long) authentication.getDetails(); // Cambia según cómo almacenas el ID en tu filtro

        // Llamar al handler para cancelar el pedido
        OrderResponseDto updatedOrder = orderHandler.cancelOrder(orderId, customerId);
        return ResponseEntity.ok(updatedOrder);
    }


    @GetMapping("/{idOrder}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long idOrder) {
        OrderResponseDto orderResponse = orderHandler.getOrderById(idOrder);
        return ResponseEntity.ok(orderResponse);
    }



}
