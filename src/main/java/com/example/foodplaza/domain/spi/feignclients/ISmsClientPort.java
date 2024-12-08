package com.example.foodplaza.domain.spi.feignclients;

public interface ISmsClientPort {
    void sendSms(String phoneNumber, String message);
}
