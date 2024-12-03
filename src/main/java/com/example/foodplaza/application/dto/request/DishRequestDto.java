package com.example.foodplaza.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
public class DishRequestDto {

    @NotBlank(message = "The name of the dish is required.")
    private String nameDish;

    @NotNull(message = "The price is required.")
    @Pattern(regexp = "^[1-9]\\d*$", message = "El precio debe ser numérico")
    private Integer price;

    @NotBlank(message = "The price must be numerical")
    private String description;

    @NotBlank(message = "Image URL is required.")
    private String urlImage;

    @NotBlank(message = "Category is required.")
    private String category;

    @NotNull(message = "Restaurant ID is required.")
    @Positive(message = "The restaurant ID must be a positive number.")
    private Long idRestaurant;

    @Override
    public String toString() {
        return "DishRequestDto{" +
                "nameDish='" + nameDish + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", urlImage='" + urlImage + '\'' +
                ", category='" + category + '\'' +
                ", idRestaurant=" + idRestaurant +
                '}';
    }

    public String getNameDish() {
        return nameDish;
    }

    public void setNameDish(String nameDish) {
        this.nameDish = nameDish;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getIdRestaurant() {
        return idRestaurant;
    }

    public void setIdRestaurant(Long idRestaurant) {
        this.idRestaurant = idRestaurant;
    }
}
