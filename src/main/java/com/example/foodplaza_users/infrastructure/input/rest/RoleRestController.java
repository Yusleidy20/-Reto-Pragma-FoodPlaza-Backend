package com.example.foodplaza_users.infrastructure.input.rest;

import com.example.foodplaza_users.application.dto.response.RoleResponseDto;
import com.example.foodplaza_users.application.handler.IRoleServiceHandler;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/user-micro/roles")
public class RoleRestController {
    private final IRoleServiceHandler roleServiceHandler;

    @GetMapping("/{idUserRole}")
    public RoleResponseDto getRoleById(@PathVariable Long idUserRole) {
        return roleServiceHandler.getRoleById(idUserRole);
    }


}
