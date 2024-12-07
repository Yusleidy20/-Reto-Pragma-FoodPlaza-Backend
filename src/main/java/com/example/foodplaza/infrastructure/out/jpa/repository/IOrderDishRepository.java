package com.example.foodplaza.infrastructure.out.jpa.repository;

import com.example.foodplaza.infrastructure.out.jpa.entity.OrderDishEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IOrderDishRepository extends JpaRepository<OrderDishEntity, Long> {
}
