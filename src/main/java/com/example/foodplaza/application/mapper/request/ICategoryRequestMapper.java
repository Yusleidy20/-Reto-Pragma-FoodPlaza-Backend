package com.example.foodplaza.application.mapper.request;

import com.example.foodplaza.application.dto.request.CategoryRequestDto;
import com.example.foodplaza.domain.model.CategoryModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ICategoryRequestMapper {
    @Mapping(source = "description", target = "description")
    CategoryModel toCategory(CategoryRequestDto categoryRequestDto);

    CategoryRequestDto toCategoryRequest(CategoryModel categoryModel);
}
