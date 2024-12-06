package com.example.foodplaza.domain.model;

import lombok.*;


@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantModel {

    private Long idRestaurant;
    private String nameRestaurant;
    private String nit;
    private String address;
    private String phoneNumber;
    private String urlLogo;
    private Long ownerId;


}
