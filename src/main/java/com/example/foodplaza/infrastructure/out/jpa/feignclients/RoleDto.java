package com.example.foodplaza.infrastructure.out.jpa.feignclients;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDto {
    private Long idUserRole;
    private String nameRole;
    private String descriptionRole;
}
