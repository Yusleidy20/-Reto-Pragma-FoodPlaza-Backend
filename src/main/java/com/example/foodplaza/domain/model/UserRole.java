package com.example.foodplaza.domain.model;


import lombok.*;



@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRole {

    private Long idUserRole;
    private String nameRole;
    private String descriptionRole;

    public Long getIdUserRole() {
        return idUserRole;
    }

    public void setIdUserRole(Long idUserRole) {
        this.idUserRole = idUserRole;
    }

    public String getNameRole() {
        return nameRole;
    }

    public void setNameRole(String nameRole) {
        this.nameRole = nameRole;
    }

    public String getDescriptionRole() {
        return descriptionRole;
    }

    public void setDescriptionRole(String descriptionRole) {
        this.descriptionRole = descriptionRole;
    }
}

