package com.example.foodplaza.application.mapper.request;

import com.example.foodplaza.application.dto.request.DishRequestDto;
import com.example.foodplaza.domain.model.DishModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DishRequestMapper { // Asigna 'true' por defecto

    @Mapping(target = "idDish", ignore = true)      // Ignora 'idDish' ya que es generado por la base de datos
    @Mapping(target = "active", constant = "true")
    DishModel toDishModel(DishRequestDto dishRequestDto);
}
