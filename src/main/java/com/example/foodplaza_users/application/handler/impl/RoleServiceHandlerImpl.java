package com.example.foodplaza_users.application.handler.impl;

import com.example.foodplaza_users.application.dto.response.RoleResponseDto;
import com.example.foodplaza_users.application.dto.resquest.RoleRequestDto;
import com.example.foodplaza_users.application.handler.IRoleServiceHandler;
import com.example.foodplaza_users.application.mapper.request.IRoleRequestMapper;
import com.example.foodplaza_users.application.mapper.response.IRoleResponseMapper;
import com.example.foodplaza_users.domain.api.IRoleServicePort;
import com.example.foodplaza_users.domain.model.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceHandlerImpl implements IRoleServiceHandler {
    private final IRoleResponseMapper roleResponseMapper;
    private final IRoleRequestMapper roleRequestMapper;
    private final IRoleServicePort roleServicePort;


    @Override
    public void saveRole(RoleRequestDto roleRequestDto) {
        UserRole role = roleRequestMapper.toRoleResponse(roleRequestDto);
        roleServicePort.saveRole(role);
    }

    @Override
    public RoleResponseDto getRoleById(Long idUserRole) {
        return roleResponseMapper.toRoleResponse(roleServicePort.getRole(idUserRole));
    }


    @Override
    public List<RoleResponseDto> getAllRoles() {
        return roleResponseMapper.toResponseList(roleServicePort.getAllRoles());
    }

    @Override
    public void deleteRoleById(Long idUserRole) {
        roleServicePort.deleteRoleById(idUserRole);
    }
}
