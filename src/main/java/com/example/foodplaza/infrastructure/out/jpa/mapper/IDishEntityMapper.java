package com.example.foodplaza.infrastructure.out.jpa.mapper;

import com.example.foodplaza.domain.model.DishModel;
import com.example.foodplaza.infrastructure.out.jpa.entity.DishEntity;
import com.example.foodplaza.infrastructure.out.jpa.entity.RestaurantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IDishEntityMapper {

    @Mapping(target = "idRestaurant", ignore = true)
    DishEntity toDishEntity(DishModel dishModel);


    @Mapping(target = "idDish", ignore = true)
    @Mapping(target = "idRestaurant", source = "idRestaurant", qualifiedByName = "mapRestaurantEntityToLong")
    DishModel toDishModel(DishEntity dishEntity);

    @Named("mapRestaurantEntityToLong")
    default Long mapRestaurantEntityToLong(RestaurantEntity restaurantEntity) {
        if (restaurantEntity == null) {
            return null;
        }
        return restaurantEntity.getIdRestaurant();
    }

}
