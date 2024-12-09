package com.example.foodplaza_traceability.infrastructure.out.jpa.feignclients.mapper;


import com.example.foodplaza_traceability.domain.model.UserModel;
import com.example.foodplaza_traceability.infrastructure.out.jpa.feignclients.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IUserDtoMapper {
    UserModel toUserModel(UserDto userDto);
}
