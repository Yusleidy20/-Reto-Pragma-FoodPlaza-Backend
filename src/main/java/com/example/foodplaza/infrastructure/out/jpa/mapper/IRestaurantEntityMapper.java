package com.example.foodplaza.infrastructure.out.jpa.mapper;

import com.example.foodplaza.domain.model.RestaurantModel;
import com.example.foodplaza.infrastructure.out.jpa.entity.RestaurantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IRestaurantEntityMapper {
    @Mapping(source = "idRestaurant", target = "idRestaurant")
    @Mapping(source = "nameRestaurant", target = "nameRestaurant")
    @Mapping(source = "nit", target = "nit")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "urlLogo", target = "urlLogo")
    @Mapping(source = "ownerId", target = "ownerId")
    RestaurantEntity toRestaurantEntity(RestaurantModel restaurantModel);
    @Mapping(source = "idRestaurant", target = "idRestaurant")
    @Mapping(source = "nameRestaurant", target = "nameRestaurant")
    @Mapping(source = "nit", target = "nit")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "urlLogo", target = "urlLogo")
    @Mapping(source = "ownerId", target = "ownerId")
    RestaurantModel toRestaurantModel(RestaurantEntity restaurantEntity);
}