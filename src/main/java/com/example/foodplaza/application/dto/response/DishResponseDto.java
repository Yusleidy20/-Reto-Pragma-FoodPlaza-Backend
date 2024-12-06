package com.example.foodplaza.application.dto.response;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DishResponseDto {
    private Long idDish;
    private String nameDish;
    private Integer price;
    private String description;
    private String urlImage;
    private Long idCategory;
    private Long idRestaurant;

}
