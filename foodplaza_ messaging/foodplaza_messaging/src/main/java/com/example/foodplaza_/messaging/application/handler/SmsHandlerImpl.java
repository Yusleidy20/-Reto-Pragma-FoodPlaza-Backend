package com.example.foodplaza_.messaging.application.handler;

import com.example.foodplaza_.messaging.application.dto.SmsRequestDto;
import com.example.foodplaza_.messaging.application.dto.SmsResponseDto;
import com.example.foodplaza_.messaging.application.mapper.SmsRequestMapper;
import com.example.foodplaza_.messaging.application.mapper.SmsResponseMapper;
import com.example.foodplaza_.messaging.domain.api.ISmsServicePort;
import com.example.foodplaza_.messaging.domain.model.SmsModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsHandlerImpl implements ISmsHandler {
    private final ISmsServicePort smsServicePort;
    private final SmsRequestMapper smsRequestMapper;
    private final SmsResponseMapper smsResponseMapper;

    @Override
    public SmsResponseDto sendSms(SmsRequestDto smsRequestDto) {
        // Mapear el DTO de solicitud al modelo del dominio
        SmsModel smsModel = smsRequestMapper.toModel(smsRequestDto);

        // Llamar al caso de uso para enviar el SMS
        SmsModel sentSms = smsServicePort.sendSms(smsModel);

        // Mapear el modelo del dominio a un DTO de respuesta
        return smsResponseMapper.toDto(sentSms);
    }
}