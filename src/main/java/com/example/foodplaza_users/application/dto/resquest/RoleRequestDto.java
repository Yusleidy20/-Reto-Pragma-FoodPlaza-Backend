package com.example.foodplaza_users.application.dto.resquest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleRequestDto {
    private Long idUserRole;
    private String nameRole;
    private String descriptionRole;
}
