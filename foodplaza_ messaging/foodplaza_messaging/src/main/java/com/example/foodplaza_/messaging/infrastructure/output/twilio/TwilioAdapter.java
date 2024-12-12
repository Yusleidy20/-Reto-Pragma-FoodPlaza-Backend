package com.example.foodplaza_.messaging.infrastructure.output.twilio;

import com.example.foodplaza_.messaging.domain.spi.ISmsPersistencePort;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import com.example.foodplaza_.messaging.domain.model.SmsModel;
import com.example.foodplaza_.messaging.infrastructure.configuration.TwilioConfig;

import lombok.RequiredArgsConstructor;



@RequiredArgsConstructor
public class TwilioAdapter implements ISmsPersistencePort {

    private final TwilioConfig twilioConfig;

    @Override
    public SmsModel sendSms(SmsModel smsModel) {
        try {
            // Crear y enviar el mensaje usando Twilio
            Message message = Message.creator(
                    new PhoneNumber(smsModel.getPhoneNumber()),
                    new PhoneNumber(twilioConfig.getFromNumber()),
                    smsModel.getMessage()
            ).create();

            return new SmsModel(
                    smsModel.getPhoneNumber(),
                    smsModel.getMessage(),
                    message.getStatus().toString(),
                    message.getSid()
            );

        } catch (ApiException e) {
            throw new IllegalStateException("Failed to send SMS: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new IllegalStateException("Unexpected error while sending SMS: " + e.getMessage(), e);
        }
    }
}
