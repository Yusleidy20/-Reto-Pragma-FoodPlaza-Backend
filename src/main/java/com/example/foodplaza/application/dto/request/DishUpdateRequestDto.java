package com.example.foodplaza.application.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DishUpdateRequestDto {

    @Min(value = 1, message = "The price must be greater than 0")
    private Integer price;  // Este es opcional ahora
    private String description;  // Este es opcional
    private Boolean active;  // Este también es opcional

}
