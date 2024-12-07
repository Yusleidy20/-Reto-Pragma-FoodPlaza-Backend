package com.example.foodplaza.infrastructure.out.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name  ="orders")
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idOrder")
    @Id
    private Long idOrder;
    @Column(name = "customerId", nullable = false)
    private Long customerId;
    @Column(name = "dateOrder", nullable = false)
    private LocalDate dateOrder;
    @Column(name = "stateOrder", nullable = false)
    private String stateOrder;
    @Column(name = "chefId")
    private Long chefId;
    @ManyToOne
    @JoinColumn(name = "idRestaurant", nullable = false)
    private RestaurantEntity restaurant;
    @OneToMany(mappedBy = "orders")
    private List<OrderDishEntity> orderDishes;

}
