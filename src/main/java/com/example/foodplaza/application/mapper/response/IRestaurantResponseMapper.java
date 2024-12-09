package com.example.foodplaza.application.mapper.response;

import com.example.foodplaza.application.dto.response.RestaurantResponseDto;
import com.example.foodplaza.domain.model.RestaurantModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRestaurantResponseMapper {
    @Mapping(source = "nameRestaurant", target = "nameRestaurant")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "nit", target = "nit")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "urlLogo", target = "urlLogo")
    RestaurantResponseDto toRestaurantResponse(RestaurantModel restaurantModel);

    List<RestaurantResponseDto> toResponseList(List<RestaurantModel> restaurantModelList);

}
