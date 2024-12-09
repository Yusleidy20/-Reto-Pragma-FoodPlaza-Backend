package com.example.foodplaza.domain.model;


import lombok.*;



@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRole {

    private Long idUserRole;
    private String nameRole;
    private String descriptionRole;

}
