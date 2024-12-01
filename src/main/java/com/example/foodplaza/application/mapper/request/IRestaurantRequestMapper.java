package com.example.foodplaza.application.mapper.request;

import com.example.foodplaza.application.dto.request.RestaurantRequestDto;
import com.example.foodplaza.domain.model.RestaurantModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IRestaurantRequestMapper {

    @Mapping(source = "idRestaurant", target = "idRestaurant")
    @Mapping(source = "nameRestaurant", target = "nameRestaurant")
    @Mapping(source = "nit", target = "nit")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "urlLogo", target = "urlLogo")
    @Mapping(source = "ownerId", target = "ownerId")
    RestaurantModel toRestaurant(RestaurantRequestDto restaurantRequestDto);

    RestaurantRequestDto toRestaurantRequest(RestaurantModel restaurantModel);
}
