package com.example.foodplaza.application.dto.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

@AllArgsConstructor
@NoArgsConstructor
public class RestaurantRequestDto {
    private Long idRestaurant;
    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^(?=.*[a-zA-Z])[0-9a-zA-Z ]+$", message = "The name can contain numbers with letters but not only numbers")
    private String nameRestaurant;
    @NotBlank(message = "The nit is required")
    @Pattern(regexp = "\\d+", message = "The nit must be numeric")
    private String nit;
    @NotBlank(message = "Address is required")
    private String address;
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\+?\\d{1,12}$", message = "The phone number must contain a maximum of 13 characters and may contain the '+' symbol at the beginning.")
    private String phoneNumber;
    @NotBlank(message = "The urlLogo is required")
    private String urlLogo;
    @NotNull(message = "The ownerId cannot be null")
    @Min(value = 1, message = "The ownerId must be greater than zero")
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private Long ownerId;

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
