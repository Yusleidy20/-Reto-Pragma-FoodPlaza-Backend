package com.example.foodplaza.application.mapper.request;

import com.example.foodplaza.application.dto.request.RestaurantRequestDto;
import com.example.foodplaza.domain.model.RestaurantModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRestaurantRequestMapper {
    @Mapping(source = "ownerId", target = "ownerId")
    RestaurantModel toRestaurant(RestaurantRequestDto restaurantRequestDto);


}
