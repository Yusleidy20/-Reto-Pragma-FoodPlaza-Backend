package com.example.foodplaza_users.application.handler;

import com.example.foodplaza_users.application.dto.response.RoleResponseDto;
import com.example.foodplaza_users.application.dto.resquest.RoleRequestDto;

import java.util.List;

public interface IRoleServiceHandler {
    void saveRole(RoleRequestDto roleRequestDto);
    RoleResponseDto getRoleById(Long idUserRole);
    List<RoleResponseDto> getAllRoles();
    void deleteRoleById(Long idUserRole);
}
