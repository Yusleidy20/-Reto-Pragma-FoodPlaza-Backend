package com.example.foodplaza.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDishModel {
    private Long idOrderDish;
    private Long idOrder;
    private DishModel dish;
    private Integer amount;


}
