package com.example.foodplaza.application.dto.response;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank(message = "El nombre del plato es requerido.")
    private String nameDish;

    @NotNull(message = "El precio es requerido.")
    @Min(value = 1, message = "El precio debe ser mayor a cero")
    private Integer price;

    @NotBlank(message = "La descripción es requerida.")
    private String description;

    @NotBlank(message = "La URL de la imagen es requerida.")
    private String urlImage;

    @NotBlank(message = "La categoría es requerida.")
    private String category;

    @NotNull(message = "El ID del restaurante es requerido.")
    @Min(value = 1, message = "El id restaurant debe ser mayor a cero")
    private Long idRestaurant;  // Por defecto, true.
}
