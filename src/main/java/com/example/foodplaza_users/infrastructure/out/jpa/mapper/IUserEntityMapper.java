package com.example.foodplaza_users.infrastructure.out.jpa.mapper;

import com.example.foodplaza_users.domain.model.UserModel;
import com.example.foodplaza_users.infrastructure.out.jpa.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUserEntityMapper {


    @Mapping(source = "userRole.idUserRole", target = "roleModel.idUserRole")
    @Mapping(source = "userRole.nameRole", target = "roleModel.nameRole")
    @Mapping(source = "userRole.descriptionRole", target = "roleModel.descriptionRole")
    UserEntity toUserEntity(UserModel userModel);

    @Mapping(source = "roleModel.idUserRole", target = "userRole.idUserRole")
    @Mapping(source = "roleModel.nameRole", target = "userRole.nameRole")
    @Mapping(source = "roleModel.descriptionRole", target = "userRole.descriptionRole")
    UserModel toUserModel(UserEntity userEntity);
}

