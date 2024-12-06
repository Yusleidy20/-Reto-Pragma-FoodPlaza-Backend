package com.example.foodplaza.infrastructure.out.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Setter
@Getter
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
    @Column(name="active",nullable = false)
    private Boolean active;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "idRestaurant", referencedColumnName = "idRestaurant", nullable = false)
    private RestaurantEntity idRestaurant;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="idCategory")
    private CategoryEntity idCategory;


}