package com.example.foodplaza.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DishRequestDto {

    @NotBlank(message = "The name of the dish is required.")
    private String nameDish;

    @NotNull(message = "The price is required.")
    @Min(value = 1, message = "The price must be numerical and greater than 0")
    private Integer price;

    @NotBlank(message = "The price must be numerical")
    private String description;

    @NotBlank(message = "Image URL is required.")
    private String urlImage;

    @NotNull(message = "Category ID is required.")
    @Positive(message = "The Category ID must be a positive number.")
    private Long idCategory;

    @NotNull(message = "Restaurant ID is required.")
    @Positive(message = "The restaurant ID must be a positive number.")
    private Long idRestaurant;

    @Override
    public String toString() {
        return "DishRequestDto{" +
                "nameDish='" + nameDish + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", urlImage='" + urlImage + '\'' +
                ", idCategory='" + idCategory + '\'' +
                ", idRestaurant=" + idRestaurant +
                '}';
    }

}
