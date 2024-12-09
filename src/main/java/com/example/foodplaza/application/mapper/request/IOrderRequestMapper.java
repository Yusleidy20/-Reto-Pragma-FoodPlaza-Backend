package com.example.foodplaza.application.mapper.request;

import com.example.foodplaza.application.dto.request.OrderDishRequestDto;
import com.example.foodplaza.application.dto.request.OrderRequestDto;
import com.example.foodplaza.domain.model.OrderDishModel;
import com.example.foodplaza.domain.model.OrderModel;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.util.List;

@Mapper(componentModel = "spring")
public interface IOrderRequestMapper {
    @Mapping(target = "restaurant.idRestaurant", source = "idRestaurant") // Mapea idRestaurant al modelo
    @Mapping(target = "idOrder", ignore = true) // Ignorar, ya que es autogenerado
    @Mapping(target = "dateOrder", ignore = true) // Se asignará la fecha actual en el caso de uso
    @Mapping(target = "stateOrder", ignore = true) // El estado se inicializa en el caso de uso
    @Mapping(target = "chefId", ignore = true) // Se asignará si aplica
    @Mapping(target = "orderDishes", source = "dishes") // Mapea los platos
    @Mapping(target = "customerId", ignore = true) // Ignorar, ya que se asigna en el caso de uso
    @Mapping(target = "securityPin", ignore = true) // Ignorar, ya que se genera en el caso de uso
    OrderModel toModel(OrderRequestDto orderRequestDto);

    @Mapping(target = "dish.idDish", source = "dishId") // Mapea el ID del plato
    @Mapping(target = "idOrder", ignore = true) // Ignorar, ya que se asigna después
    @Mapping(target = "idOrderDish", ignore = true) // Ignorar, ya que es autogenerado
    OrderDishModel toOrderDishModel(OrderDishRequestDto dishRequestDto);

    List<OrderDishModel> toOrderDishModels(List<OrderDishRequestDto> dishRequestDtos);
}
