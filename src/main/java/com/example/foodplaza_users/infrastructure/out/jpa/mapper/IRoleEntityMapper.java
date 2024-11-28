package com.example.foodplaza_users.infrastructure.out.jpa.mapper;

import com.example.foodplaza_users.domain.model.UserRole;
import com.example.foodplaza_users.infrastructure.out.jpa.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface IRoleEntityMapper {
    List<UserRole> toRoleList(List<RoleEntity> roleEntityList);
    RoleEntity toEntity(UserRole role);
    UserRole toRoleModel(RoleEntity roleEntity);
}
