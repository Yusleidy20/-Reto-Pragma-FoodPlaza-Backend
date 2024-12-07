package com.example.foodplaza.infrastructure.out.jpa.mapper;

import com.example.foodplaza.domain.model.OrderDishModel;
import com.example.foodplaza.infrastructure.out.jpa.entity.OrderDishEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IOrderDishEntityMapper {


    OrderDishEntity toEntity(OrderDishModel orderDishModel);


    OrderDishModel toModel(OrderDishEntity orderDishEntity);

    List<OrderDishEntity> toEntityList(List<OrderDishModel> orderDishModels);
    List<OrderDishModel> toModelList(List<OrderDishEntity> orderDishEntities);
}
