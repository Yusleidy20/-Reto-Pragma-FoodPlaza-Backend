package com.example.foodplaza.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DishRequestDto {

    @NotBlank(message = "The name of the dish is required.")
    private String nameDish;

    @NotNull(message = "The price is required.")
    @Pattern(regexp = "^[1-9]\\d*$", message = "El precio debe ser numérico")
    private Integer price;

    @NotBlank(message = "The price must be numerical")
    private String description;

    @NotBlank(message = "Image URL is required.")
    private String urlImage;

    @NotBlank(message = "Category is required.")
    private String category;

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
                ", category='" + category + '\'' +
                ", idRestaurant=" + idRestaurant +
                '}';
    }
}
