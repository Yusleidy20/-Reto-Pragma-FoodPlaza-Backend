package com.example.foodplaza.application.dto.response;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryResponseDto {
    private Long idCategory;
    private String nameCategory;
    private String description;

}
