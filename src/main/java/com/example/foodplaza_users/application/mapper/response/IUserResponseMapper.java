package com.example.foodplaza_users.application.mapper.response;

import com.example.foodplaza_users.application.dto.response.UserResponseDto;
import com.example.foodplaza_users.domain.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUserResponseMapper {

    @Mapping(source = "userRole.idUserRole", target = "idUserRole")
    UserResponseDto toUserResponse(UserModel userModel);
}
