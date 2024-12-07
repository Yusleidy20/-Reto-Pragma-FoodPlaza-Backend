package com.example.foodplaza.domain.usecase;

import com.example.foodplaza.domain.api.IRestaurantServicePort;
import com.example.foodplaza.domain.api.IValidatorServicePort;
import com.example.foodplaza.domain.model.OrderDishModel;
import com.example.foodplaza.domain.model.OrderModel;
import com.example.foodplaza.domain.spi.persistence.IOrderPersistencePort;
import com.example.foodplaza.infrastructure.configuration.Constants;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class ValidatorService implements IValidatorServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    @Override
    public void validateOrder(OrderModel orderModel) {
        validateActiveOrders(orderModel.getCustomerId());
        validateOrderDishes(orderModel.getOrderDishes());
        validateRestaurantAndDishes(orderModel.getRestaurant().getIdRestaurant(), orderModel.getOrderDishes());
    }

    @Override
    public void validatePendingOrder(OrderModel order) {
        if (!Constants.PENDING.equals(order.getStateOrder())) {
            throw new IllegalStateException("Only pending orders can be updated.");
        }
    }

    private void validateActiveOrders(Long customerId) {
        List<OrderModel> activeOrders = orderPersistencePort.getOrdersByCustomerId(customerId);
        boolean hasActiveOrders = activeOrders.stream()
                .anyMatch(order -> isActiveState(order.getStateOrder()));

        if (hasActiveOrders) {
            throw new IllegalStateException("The customer already has an active order.");
        }
    }

    private void validateOrderDishes(List<OrderDishModel> orderDishes) {
        if (orderDishes.isEmpty()) {
            throw new IllegalArgumentException("The order must contain at least one dish.");
        }

        orderDishes.forEach(dish -> {
            if (dish.getAmount() == null || dish.getAmount() <= 0) {
                throw new IllegalArgumentException("Each dish must have a positive quantity.");
            }
        });
    }

    private void validateRestaurantAndDishes(Long restaurantId, List<OrderDishModel> orderDishes) {
        List<Long> dishIds = orderDishes.stream()
                .map(dish -> dish.getDish().getIdDish())
                .toList();

        boolean allDishesBelongToRestaurant = orderPersistencePort.validateDishesBelongToRestaurant(restaurantId, dishIds);

        if (!allDishesBelongToRestaurant) {
            throw new IllegalArgumentException("All dishes must belong to the specified restaurant.");
        }
    }

    private boolean isActiveState(String state) {
        return Constants.PENDING.equals(state) || Constants.IN_PREPARATION.equals(state) || Constants.READY.equals(state);
    }
}
