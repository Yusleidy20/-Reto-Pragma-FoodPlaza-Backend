package com.example.foodplaza.application.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DishUpdateRequestDto {

    @Min(value = 1, message = "The price must be greater than 0")
    private Integer price;
    private String description;
    private Boolean active;


}
