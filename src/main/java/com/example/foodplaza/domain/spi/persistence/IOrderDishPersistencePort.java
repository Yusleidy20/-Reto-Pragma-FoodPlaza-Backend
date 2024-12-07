package com.example.foodplaza.domain.spi.persistence;

import com.example.foodplaza.domain.model.OrderDishModel;

import java.util.List;

public interface IOrderDishPersistencePort {
    void saveOrderDishes(List<OrderDishModel> orderDishModels);
}
