package com.example.foodplaza_.messaging.application.handler;

import com.example.foodplaza_.messaging.application.dto.SmsRequestDto;
import com.example.foodplaza_.messaging.application.dto.SmsResponseDto;

public interface ISmsHandler {
    SmsResponseDto sendSms(SmsRequestDto smsRequestDto);
}