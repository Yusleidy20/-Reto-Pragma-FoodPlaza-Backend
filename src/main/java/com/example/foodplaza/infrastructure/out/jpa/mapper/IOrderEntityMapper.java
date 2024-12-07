package com.example.foodplaza.infrastructure.out.jpa.mapper;

import com.example.foodplaza.domain.model.OrderModel;
import com.example.foodplaza.infrastructure.out.jpa.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {IOrderDishEntityMapper.class})
public interface IOrderEntityMapper {

    OrderEntity toEntity(OrderModel orderModel);

    OrderModel toModel(OrderEntity orderEntity);
}
