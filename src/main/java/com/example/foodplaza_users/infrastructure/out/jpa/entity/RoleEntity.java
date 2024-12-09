package com.example.foodplaza_users.infrastructure.out.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUserRole",nullable = false)
    private Long idUserRole;
    @Column(name = "nameRole")
    private String nameRole;
    @Column(name = "descriptionRole")
    private String descriptionRole;

}
