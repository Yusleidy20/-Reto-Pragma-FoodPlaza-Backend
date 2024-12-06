package com.example.foodplaza.infrastructure.out.jpa.mapper;

import com.example.foodplaza.domain.model.CategoryModel;
import com.example.foodplaza.infrastructure.out.jpa.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface ICategoryEntityMapper {

    CategoryEntity toCategoryEntity(CategoryModel categoryModel);
    CategoryModel toCategoryModel(CategoryEntity categoryEntity);
    List<CategoryModel> toCategoryModelList(List<CategoryEntity> categoryEntities);
}
