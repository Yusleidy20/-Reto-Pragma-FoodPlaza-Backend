package com.example.foodplaza_traceability.infrastructure.out.jpa.feignclients.mapper;


import com.example.foodplaza_traceability.application.dto.RoleResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "role-service", url = "http://localhost:8096")  // Cambia la URL si es necesario
public interface IRoleFeignClient {
    @GetMapping("/roles/{idUserRole}")
    RoleResponseDto getRoleById(@PathVariable("idUserRole") Long idUserRole);
}
