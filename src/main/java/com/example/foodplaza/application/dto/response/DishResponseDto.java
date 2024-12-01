package com.example.foodplaza.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DishResponseDto {
    private Long idDish;
    private String nameDish;
    private Integer price;
    private String description;
    private String urlImage;
    private String category;
    private Long idRestaurant;
}
