package com.example.foodplaza.infrastructure.out.jpa.entity;


import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "restaurant")
public class RestaurantEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRestaurant")
    @Id
    private Long idRestaurant;
    @Column(name = "nameRestaurant", nullable = false)
    private String nameRestaurant;
    @Column(name = "nit", unique = true, nullable = false)
    private String nit;
    @Column(name = "address", nullable = false)
    private String address;
    @Column(name = "phoneNumber", nullable = false)
    private String phoneNumber;
    @Column(name = "urlLogo", nullable = false)
    private String urlLogo;
    @Column(name = "ownerId", nullable = false)
    private Long ownerId ;

}
