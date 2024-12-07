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

}
