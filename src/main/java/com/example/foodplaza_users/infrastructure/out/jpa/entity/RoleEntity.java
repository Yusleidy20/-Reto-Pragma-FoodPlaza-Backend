package com.example.foodplaza_users.infrastructure.out.jpa.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "roles")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUserRole",nullable = false)
    private Long idUserRole;
    @Column(name = "nameRole")
    @NotBlank
    private String nameRole;
    @Column(name = "descriptionRole")
    @NotBlank
    private String descriptionRole;



}
