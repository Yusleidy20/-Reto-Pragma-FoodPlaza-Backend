package com.example.foodplaza.application.mapper.response;

import com.example.foodplaza.application.dto.response.OrderDishResponseDto;
import com.example.foodplaza.application.dto.response.OrderResponseDto;
import com.example.foodplaza.domain.model.OrderDishModel;
import com.example.foodplaza.domain.model.OrderModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface IOrderResponseMapper {

    // Mapea la entidad principal
    @Mapping(target = "dishes", source = "orderDishes")  // Nombre debe coincidir con el atributo en OrderResponseDto
    @Mapping(target = "chefId", source = "chefId")
    OrderResponseDto toDto(OrderModel orderModel);

    // Mapea los platos individuales
    @Mapping(target = "nameDish", source = "dish.nameDish")  // Atributo nameDish en DishModel
    @Mapping(target = "amount", source = "amount")            // Atributo amount en OrderDishModel
    OrderDishResponseDto toDishResponseDto(OrderDishModel orderDishModel);

    // Convierte la lista de platos
    default List<OrderDishResponseDto> toDishResponseDtoList(List<OrderDishModel> orderDishModels) {
        if (orderDishModels == null) {
            return new ArrayList<>();
        }
        return orderDishModels.stream()
                .map(this::toDishResponseDto)
                .toList();
    }
}
