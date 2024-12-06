package com.example.foodplaza.infrastructure.out.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name  ="order_dish")
@NoArgsConstructor
@AllArgsConstructor
public class OrderDishEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idOrderDish")
    @Id
    private Long idOrderDish;
    @ManyToOne
    @JoinColumn(name = "idOrder", nullable = false)
    private OrderEntity orders;
    @ManyToOne
    @JoinColumn(name = "idDish", nullable = false)
    private DishEntity dish;
    @Column(name = "amount", nullable = false)
    private Integer amount;




}
