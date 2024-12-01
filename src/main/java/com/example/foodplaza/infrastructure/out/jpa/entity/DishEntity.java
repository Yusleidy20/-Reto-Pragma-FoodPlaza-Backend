package com.example.foodplaza.infrastructure.out.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "dishes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idDish",nullable = false)
    @Id
    private Long idDish;

    @Column(name="nameDish",nullable = false)
    private String nameDish;  // Aquí el nombre de la propiedad es 'nameDish'

    @Column(name="price",nullable = false)
    private Integer price;

    @Column(name="description",nullable = false, length = 500)
    private String description;

    @Column(name="urlImage",nullable = false)
    private String urlImage;  // Aquí el nombre es 'urlImage'

    @Column(name="category", nullable = false)
    private String category;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "idRestaurant", referencedColumnName = "idRestaurant", nullable = false)
    private RestaurantEntity idRestaurant;  // Aquí es 'idRestaurant'

    @Column(name="active",nullable = false)
    private Boolean active;


}