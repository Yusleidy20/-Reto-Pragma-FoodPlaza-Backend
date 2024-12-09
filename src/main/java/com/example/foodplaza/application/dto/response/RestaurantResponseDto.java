package com.example.foodplaza.application.dto.response;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RestaurantResponseDto {
    private String nameRestaurant;
    private String nit;
    private String address;
    private String phoneNumber;
    private String urlLogo;
    private Long ownerId;

}
