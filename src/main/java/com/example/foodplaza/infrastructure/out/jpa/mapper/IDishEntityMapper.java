package com.example.foodplaza.infrastructure.out.jpa.mapper;

import com.example.foodplaza.domain.model.DishModel;
import com.example.foodplaza.infrastructure.out.jpa.entity.DishEntity;
import com.example.foodplaza.infrastructure.out.jpa.entity.RestaurantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE, // Ignorar propiedades no mapeadas en el destino
        unmappedSourcePolicy = ReportingPolicy.IGNORE // Ignorar propiedades no mapeadas en el origen
)
public interface IDishEntityMapper {

    // Convierte DishModel a DishEntity
    @Mapping(source = "idRestaurant", target = "idRestaurant", qualifiedByName = "mapRestaurant")
    @Mapping(source = "idCategory", target = "idCategory")
    DishEntity toDishEntity(DishModel dishModel);

    // Convierte DishEntity a DishModel
    @Mapping(source = "idRestaurant.idRestaurant", target = "idRestaurant")
    @Mapping(source = "idCategory", target = "idCategory")
    DishModel toDishModel(DishEntity dishEntity);

    // Método personalizado para manejar RestaurantEntity a Long
    @Named("mapRestaurant")
    default RestaurantEntity mapRestaurant(Long idRestaurant) {
        if (idRestaurant == null) {
            return null;
        }
        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setIdRestaurant(idRestaurant);
        return restaurant;
    }
}
