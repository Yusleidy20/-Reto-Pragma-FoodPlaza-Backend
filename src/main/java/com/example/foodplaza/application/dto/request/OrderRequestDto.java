package com.example.foodplaza.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    private Long customerId;
    @NotNull(message = "Restaurant ID is required.")
    @Positive(message = "Restaurant ID must be a positive number.")
    private Long idRestaurant;
    private List<OrderDishRequestDto> dishes;
}
