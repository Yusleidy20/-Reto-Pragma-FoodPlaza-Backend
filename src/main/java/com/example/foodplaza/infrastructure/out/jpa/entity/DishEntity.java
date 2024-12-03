package com.example.foodplaza.infrastructure.out.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "dishes")

@AllArgsConstructor
@NoArgsConstructor
public class DishEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idDish",nullable = false)
    @Id
    private Long idDish;

    @Column(name="nameDish",nullable = false)
    private String nameDish;
    @Column(name="price",nullable = false)
    private Integer price;

    @Column(name="description",nullable = false, length = 500)
    private String description;

    @Column(name="urlImage",nullable = false)
    private String urlImage;

    @Column(name="category", nullable = false)
    private String category;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "idRestaurant", referencedColumnName = "idRestaurant", nullable = false)
    private RestaurantEntity idRestaurant;

    @Column(name="active",nullable = false)
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

    public RestaurantEntity getIdRestaurant() {
        return idRestaurant;
    }

    public void setIdRestaurant(RestaurantEntity idRestaurant) {
        this.idRestaurant = idRestaurant;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}