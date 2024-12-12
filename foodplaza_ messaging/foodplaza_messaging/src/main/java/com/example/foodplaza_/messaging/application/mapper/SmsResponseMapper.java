package com.example.foodplaza_.messaging.application.mapper;

import com.example.foodplaza_.messaging.application.dto.SmsResponseDto;
import com.example.foodplaza_.messaging.domain.model.SmsModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SmsResponseMapper {
    @Mapping(source = "status", target = "status")
    @Mapping(source = "messageId", target = "messageId")
    SmsResponseDto toDto(SmsModel smsModel);
}
