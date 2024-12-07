package com.example.foodplaza.infrastructure.input.rest;

import com.example.foodplaza.application.dto.request.OrderRequestDto;
import com.example.foodplaza.application.dto.request.RestaurantRequestDto;
import com.example.foodplaza.application.dto.response.OrderResponseDto;
import com.example.foodplaza.application.handler.IOrderHandlerPort;
import io.jsonwebtoken.Jwt;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    @PostMapping("/orders/{orderId}/assign-chef")
    @PreAuthorize("hasAuthority('Employee')")
    public ResponseEntity<OrderResponseDto> assignChefToOrder(@PathVariable Long orderId) {
        // Obtener el ID del chef desde la autenticación
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long chefId = (Long) authentication.getDetails();

        // Asignar el chef
        OrderResponseDto updatedOrder = orderHandler.assignChefToOrder(orderId, chefId);
        return ResponseEntity.ok(updatedOrder);
    }

    @PutMapping("/orders/{orderId}/mark-as-ready")
    @PreAuthorize("hasAuthority('Chef')")
    public ResponseEntity<OrderResponseDto> markOrderAsReady(@PathVariable Long orderId) {
        // Marcar el pedido como listo
        OrderResponseDto updatedOrder = orderHandler.markOrderAsReady(orderId);
        return ResponseEntity.ok(updatedOrder);
    }

    @GetMapping("/{idOrder}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long idOrder) {
        OrderResponseDto orderResponse = orderHandler.getOrderById(idOrder);
        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByCustomerId(@PathVariable Long customerId) {
        List<OrderResponseDto> orders = orderHandler.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }
}
