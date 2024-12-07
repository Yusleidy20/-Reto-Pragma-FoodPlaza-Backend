package com.example.foodplaza.domain.model;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DishModel {
    private Long idDish;
    private String nameDish;
    private Integer price;
    private String description;
    private String urlImage;
    private CategoryModel idCategory;
    private Long idRestaurant;
    private Boolean active;

}
