package com.example.foodplaza_.messaging.application.mapper;

import com.example.foodplaza_.messaging.application.dto.SmsRequestDto;
import com.example.foodplaza_.messaging.domain.model.SmsModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SmsRequestMapper {
    SmsModel toModel(SmsRequestDto smsRequestDto);
}