package com.example.foodplaza.domain.api;

import com.example.foodplaza.domain.model.OrderModel;

public interface IValidatorServicePort {
    void validateOrder(OrderModel orderModel);
    void validatePendingOrder(OrderModel order);
}
