package com.example.foodplaza.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DishListResponseDto {
    private String nameDish;
    private Integer price;
    private String description;
    private String urlImage;
}
