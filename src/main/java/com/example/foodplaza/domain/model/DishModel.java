package com.example.foodplaza.domain.model;

import lombok.*;

@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishModel {
    private Long idDish;
    private String nameDish;
    private Integer price;
    private String description;
    private String urlImage;
    private String category;
    private Long idRestaurant;
    private Boolean active;

    public Long getIdDish() {
        return idDish;
    }

    public void setIdDish(Long idDish) {
        this.idDish = idDish;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
