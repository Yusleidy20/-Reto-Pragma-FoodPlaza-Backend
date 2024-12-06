package com.example.foodplaza.application.mapper.response;

import com.example.foodplaza.application.dto.response.CategoryResponseDto;
import com.example.foodplaza.domain.model.CategoryModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ICategoryResponseMapper {
    CategoryResponseDto toResponse(CategoryModel categoryModel);
    List<CategoryResponseDto> toCategoryResponseList(List<CategoryModel> categoryModelList);
}
