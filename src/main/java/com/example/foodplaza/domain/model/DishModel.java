package com.example.foodplaza.domain.model;

import lombok.*;

@ToString
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishModel {
    private Long idDish;
    private String nameDish;
    private Integer price;
    private String description;
    private String urlImage;
    private String category;
    private Long idRestaurant;
    private Boolean active;



}
