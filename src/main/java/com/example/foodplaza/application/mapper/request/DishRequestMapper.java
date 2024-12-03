package com.example.foodplaza.application.mapper.request;

import com.example.foodplaza.application.dto.request.DishRequestDto;
import com.example.foodplaza.domain.model.DishModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DishRequestMapper {

    @Mapping(target = "idDish", ignore = true)
    DishModel toDishModel(DishRequestDto dishRequestDto);
}
