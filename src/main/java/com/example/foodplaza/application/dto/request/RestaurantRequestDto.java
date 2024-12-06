package com.example.foodplaza.application.dto.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

@Setter
@Getter
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

}
