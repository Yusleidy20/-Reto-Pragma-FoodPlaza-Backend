package com.example.foodplaza.application.mapper.request;

import com.example.foodplaza.application.dto.request.DishRequestDto;
import com.example.foodplaza.domain.model.CategoryModel;
import com.example.foodplaza.domain.model.DishModel;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IDishRequestMapper {

    @Mapping(target = "idCategory", source = "idCategory", qualifiedByName = "mapIdToCategoryModel") // Usa el método personalizado
    DishModel toDishModel(DishRequestDto dishRequestDto);

    @Named("mapIdToCategoryModel")
    default CategoryModel mapIdToCategoryModel(Long idCategory) {
        if (idCategory == null) {
            return null; // Manejo de valores nulos
        }
        return CategoryModel.builder()
                .idCategory(idCategory)
                .build();
    }
}
