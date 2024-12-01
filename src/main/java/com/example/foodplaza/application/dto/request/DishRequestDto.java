package com.example.foodplaza.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishRequestDto {

    @NotBlank(message = "El nombre del plato es requerido.")
    private String nameDish;

    @NotNull(message = "El precio es requerido.")
    @Pattern(regexp = "^[1-9]\\d*$", message = "El precio debe ser numérico")
    private Integer price;

    @NotBlank(message = "La descripción es requerida.")
    private String description;

    @NotBlank(message = "La URL de la imagen es requerida.")
    private String urlImage;

    @NotBlank(message = "La categoría es requerida.")
    private String category;

    @NotNull(message = "El ID del restaurante es requerido.")
    @Positive(message = "El ID del restaurante debe ser un número positivo.")
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
