package com.example.foodplaza_users.infrastructure.input.rest;

import com.example.foodplaza_users.application.dto.response.RoleResponseDto;
import com.example.foodplaza_users.application.dto.resquest.RoleRequestDto;
import com.example.foodplaza_users.application.handler.IRoleServiceHandler;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "user-micro/roles")
public class RoleRestController {
    private final IRoleServiceHandler roleServiceHandler;


    @PostMapping("/")
    public ResponseEntity<Void> createRole(@RequestBody RoleRequestDto roleRequestDto) {
        // Log para verificar los datos recibidos
        if (roleRequestDto.getNameRole() == null || roleRequestDto.getDescriptionRole() == null) {
            throw new IllegalArgumentException("Role fields cannot be null");
        }
        roleServiceHandler.saveRole(roleRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping("/{idUserRole}")
    public RoleResponseDto getRoleById(@PathVariable Long idUserRole) {
        return roleServiceHandler.getRoleById(idUserRole);
    }


    @DeleteMapping("/{idUserRole}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long idUserRole) {
        roleServiceHandler.deleteRoleById(idUserRole);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // Retorna código 204 si la eliminación es exitosa
    }


}
