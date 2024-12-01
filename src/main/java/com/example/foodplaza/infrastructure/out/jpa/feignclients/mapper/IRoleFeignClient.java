package com.example.foodplaza.infrastructure.out.jpa.feignclients.mapper;

import com.example.foodplaza.application.dto.response.RoleResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "role-service", url = "http://localhost:8096")  // Cambia la URL si es necesario
public interface IRoleFeignClient {
    @GetMapping("/roles/{roleId}")
    RoleResponseDto getRoleById(@PathVariable("roleId") Long roleId);
}
