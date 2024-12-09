package com.example.foodplaza_traceability.infrastructure.out.jpa.feignclients;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDto {
    private Long idRestaurant;
    private String name;
}
