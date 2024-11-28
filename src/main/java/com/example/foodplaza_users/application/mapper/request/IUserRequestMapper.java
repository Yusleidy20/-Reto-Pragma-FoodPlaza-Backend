package com.example.foodplaza_users.application.mapper.request;


import com.example.foodplaza_users.domain.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUserRequestMapper {
    @Mapping(source = "birthDate", target = "birthDate")
    UserModel toUser(UserModel userRequestDto);
}

