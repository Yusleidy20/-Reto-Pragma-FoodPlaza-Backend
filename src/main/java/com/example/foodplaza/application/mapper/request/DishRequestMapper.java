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
public interface DishRequestMapper {

    @Mapping(target = "idDish", ignore = true)  // Ignorar el ID
    @Mapping(target = "active", constant = "true")  // Establecer "active" en true
    @Mapping(target = "idCategory", source = "idCategory", qualifiedByName = "mapLongToCategoryModel")  // Mapeo personalizado para CategoryModel
    DishModel toDishModel(DishRequestDto dishRequestDto);

    @Named("mapLongToCategoryModel")
    default CategoryModel mapLongToCategoryModel(Long idCategory) {
        if (idCategory == null) {
            return null;  // Si no hay ID, no se asigna ninguna categoría
        }
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setIdCategory(idCategory);  // Asignamos el ID de la categoría
        return categoryModel;
    }
}
