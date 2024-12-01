package com.example.foodplaza.application.mapper.response;

import com.example.foodplaza.application.dto.response.DishResponseDto;
import com.example.foodplaza.domain.model.DishModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface DishResponseMapper {

    DishResponseDto toDishResponse(DishModel dishModel);
}
