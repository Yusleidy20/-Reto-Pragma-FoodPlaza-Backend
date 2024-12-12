package com.example.foodplaza_.messaging.domain.usecase;

import com.example.foodplaza_.messaging.domain.api.ISmsServicePort;
import com.example.foodplaza_.messaging.domain.model.SmsModel;
import com.example.foodplaza_.messaging.domain.spi.ISmsPersistencePort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SmsUseCase implements ISmsServicePort {

    private final ISmsPersistencePort smsPersistencePort;

    @Override
    public SmsModel sendSms(SmsModel smsModel) {
        // Validaciones básicas
        if (smsModel.getPhoneNumber() == null || smsModel.getPhoneNumber().isBlank()) {
            throw new IllegalArgumentException("Phone number is required.");
        }
        if (smsModel.getMessage() == null || smsModel.getMessage().isBlank()) {
            throw new IllegalArgumentException("Message cannot be empty.");
        }

        // Lógica adicional (ejemplo: prefijo internacional, validación de formato)
        if (!smsModel.getPhoneNumber().startsWith("+")) {
            throw new IllegalArgumentException("Phone number must include country code (e.g., +123).");
        }

        // Delegar la operación al adaptador de infraestructura
        return smsPersistencePort.sendSms(smsModel);
    }
}