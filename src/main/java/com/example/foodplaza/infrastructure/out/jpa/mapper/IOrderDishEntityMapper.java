package com.example.foodplaza.infrastructure.out.jpa.mapper;

import com.example.foodplaza.domain.model.OrderDishModel;
import com.example.foodplaza.infrastructure.out.jpa.entity.OrderDishEntity;
import com.example.foodplaza.infrastructure.out.jpa.entity.RestaurantEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IOrderDishEntityMapper {


    OrderDishEntity toEntity(OrderDishModel orderDishModel);


    OrderDishModel toModel(OrderDishEntity orderDishEntity);

    List<OrderDishEntity> toEntityList(List<OrderDishModel> orderDishModels);
    List<OrderDishModel> toModelList(List<OrderDishEntity> orderDishEntities);


    // Método para mapear Long a RestaurantEntity
    default RestaurantEntity map(Long idRestaurant) {
        if (idRestaurant == null) {
            return null;
        }
        RestaurantEntity restaurantEntity = new RestaurantEntity();
        restaurantEntity.setIdRestaurant(idRestaurant);
        return restaurantEntity;
    }

    // Método para mapear RestaurantEntity a Long
    default Long map(RestaurantEntity restaurantEntity) {
        return restaurantEntity != null ? restaurantEntity.getIdRestaurant() : null;
    }
}
