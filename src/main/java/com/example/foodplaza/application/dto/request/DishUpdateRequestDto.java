package com.example.foodplaza.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DishUpdateRequestDto {
    @NotNull(message = "The price is required.")
    @Min(value = 1, message = "The price must be greater than 0")
    private Integer price;
    @NotBlank(message = "Description is required.")
    private String description;

}
