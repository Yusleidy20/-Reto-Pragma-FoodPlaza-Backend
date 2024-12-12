package com.example.foodplaza_.messaging.domain.api;

import com.example.foodplaza_.messaging.domain.model.SmsModel;

public interface ISmsServicePort {
    SmsModel sendSms(SmsModel smsModel);
}
