package com.example.foodplaza_.messaging.domain.spi;

import com.example.foodplaza_.messaging.domain.model.SmsModel;

public interface ISmsPersistencePort {
    SmsModel sendSms(SmsModel smsModel);
}