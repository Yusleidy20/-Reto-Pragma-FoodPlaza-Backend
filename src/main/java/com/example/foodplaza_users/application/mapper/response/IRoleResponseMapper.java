package com.example.foodplaza_users.application.mapper.response;

import com.example.foodplaza_users.application.dto.response.RoleResponseDto;
import com.example.foodplaza_users.domain.model.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface IRoleResponseMapper {

    RoleResponseDto toRoleResponse(UserRole userRole);

    List<RoleResponseDto> toResponseList(List<UserRole> roleList);

}
