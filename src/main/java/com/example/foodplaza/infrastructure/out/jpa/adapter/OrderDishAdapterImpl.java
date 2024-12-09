package com.example.foodplaza.infrastructure.out.jpa.adapter;

import com.example.foodplaza.domain.model.DishModel;
import com.example.foodplaza.domain.model.OrderDishModel;
import com.example.foodplaza.domain.spi.persistence.IOrderDishPersistencePort;
import com.example.foodplaza.infrastructure.out.jpa.entity.DishEntity;
import com.example.foodplaza.infrastructure.out.jpa.entity.OrderDishEntity;
import com.example.foodplaza.infrastructure.out.jpa.entity.OrderEntity;
import com.example.foodplaza.infrastructure.out.jpa.mapper.IOrderDishEntityMapper;
import com.example.foodplaza.infrastructure.out.jpa.repository.IOrderDishRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class OrderDishAdapterImpl implements IOrderDishPersistencePort {

    private final IOrderDishRepository orderDishRepository;
    private final IOrderDishEntityMapper orderDishEntityMapper;

    @Override
    public void saveOrderDishes(List<OrderDishModel> orderDishModels) {
        List<OrderDishEntity> entities = orderDishModels.stream()
                .map(this::mapToEntity) // Aquí se utiliza el método
                .toList();
        orderDishRepository.saveAll(entities);
    }

    private OrderDishEntity mapToEntity(OrderDishModel model) {
        OrderDishEntity entity = new OrderDishEntity();

        // Asignar ID de pedido si está disponible
        if (model.getIdOrder() != null) {
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setIdOrder(model.getIdOrder());
            entity.setOrders(orderEntity);
        }

        // Asignar plato si está disponible
        if (model.getDish() != null) {
            DishEntity dishEntity = new DishEntity();
            dishEntity.setIdDish(model.getDish().getIdDish());
            entity.setDish(dishEntity);
        }

        entity.setAmount(model.getAmount() != null ? model.getAmount() : 0); // Default a 0 si es nulo

        return entity;
    }



}
