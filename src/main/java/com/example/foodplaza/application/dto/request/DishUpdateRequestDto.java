package com.example.foodplaza.application.dto.request;

import jakarta.validation.constraints.Min;

public class DishUpdateRequestDto {

    @Min(value = 1, message = "The price must be greater than 0")
    private Integer price;  // Este es opcional ahora
    private String description;  // Este es opcional
    private Boolean active;  // Este también es opcional

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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
