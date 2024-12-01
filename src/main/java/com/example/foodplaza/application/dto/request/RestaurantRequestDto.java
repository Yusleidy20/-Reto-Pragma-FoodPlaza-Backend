package com.example.foodplaza.application.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantRequestDto {
    private Long idRestaurant;
    private String nameRestaurant;
    private String nit;
    private String address;
    private String phoneNumber;
    private String urlLogo;
    private Long ownerId;

}
