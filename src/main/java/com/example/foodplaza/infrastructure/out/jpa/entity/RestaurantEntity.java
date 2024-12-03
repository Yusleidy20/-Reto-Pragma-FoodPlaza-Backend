package com.example.foodplaza.infrastructure.out.jpa.entity;


import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
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

    public Long getIdRestaurant() {
        return idRestaurant;
    }

    public void setIdRestaurant(Long idRestaurant) {
        this.idRestaurant = idRestaurant;
    }

    public String getNameRestaurant() {
        return nameRestaurant;
    }

    public void setNameRestaurant(String nameRestaurant) {
        this.nameRestaurant = nameRestaurant;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}
