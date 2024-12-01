package com.example.foodplaza_users.application.mapper.response;

import com.example.foodplaza_users.application.dto.response.UserResponseDto;
import com.example.foodplaza_users.domain.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUserResponseMapper {

    UserResponseDto toUserResponse(UserModel userModel);
}
