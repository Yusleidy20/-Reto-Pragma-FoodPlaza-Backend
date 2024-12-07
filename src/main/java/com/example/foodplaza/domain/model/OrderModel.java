package com.example.foodplaza.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderModel {
    private Long idOrder;
    private Long customerId;
    private LocalDate dateOrder;
    private String stateOrder; // Puedes usar un enum si es más conveniente
    private Long chefId;
    private RestaurantModel restaurant;
    private List<OrderDishModel> orderDishes = new ArrayList<>();
}

