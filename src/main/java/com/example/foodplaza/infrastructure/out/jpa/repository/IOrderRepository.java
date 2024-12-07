package com.example.foodplaza.infrastructure.out.jpa.repository;

import com.example.foodplaza.infrastructure.out.jpa.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByCustomerId(Long customerId);
    // Nuevo método para verificar pedidos activos
    List<OrderEntity> findByCustomerIdAndStateOrderIn(Long customerId, List<String> states);
}
