package com.example.foodplaza.domain.model;

import lombok.*;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryModel {
    private Long idCategory;
    private String nameCategory;
    private String description;

}
