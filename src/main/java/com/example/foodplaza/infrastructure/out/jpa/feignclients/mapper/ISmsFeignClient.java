package com.example.foodplaza.infrastructure.out.jpa.feignclients.mapper;

import com.example.foodplaza.application.dto.request.SmsRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "messaging-service", url = "http://localhost:8081")
public interface ISmsFeignClient {

    @PostMapping("/sms/send")
    void sendSms(@RequestBody SmsRequestDto smsRequestDto);
}
