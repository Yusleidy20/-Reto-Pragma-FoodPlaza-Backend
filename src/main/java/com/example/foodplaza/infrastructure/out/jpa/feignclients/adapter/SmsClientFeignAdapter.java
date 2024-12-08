package com.example.foodplaza.infrastructure.out.jpa.feignclients.adapter;

import com.example.foodplaza.application.dto.request.SmsRequestDto;
import com.example.foodplaza.domain.spi.feignclients.ISmsClientPort;
import com.example.foodplaza.infrastructure.out.jpa.feignclients.mapper.ISmsFeignClient;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SmsClientFeignAdapter implements ISmsClientPort {

    private final ISmsFeignClient smsFeignClient;

    @Override
    public void sendSms(String phoneNumber, String message) {
        // Crea el DTO de solicitud
        SmsRequestDto smsRequestDto = new SmsRequestDto(phoneNumber, message);

        // Llama al cliente Feign
        smsFeignClient.sendSms(smsRequestDto);
    }
}