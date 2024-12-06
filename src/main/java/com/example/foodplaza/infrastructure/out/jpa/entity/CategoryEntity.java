package com.example.foodplaza.infrastructure.out.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Setter
@Getter
@Entity
@Table(name  ="category")
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCategory")
    @Id
    private Long idCategory;
    @Column(name = "nameCategory", nullable = false)
    private String nameCategory;
    @Column(name = "description", nullable = false)
    private String description;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL,mappedBy = "idCategory")
    private List<DishEntity> dishes;

}
