package com.example.foodplaza_.messaging.infrastructure.input.rest;

import com.example.foodplaza_.messaging.application.dto.SmsRequestDto;
import com.example.foodplaza_.messaging.application.dto.SmsResponseDto;
import com.example.foodplaza_.messaging.application.handler.ISmsHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
public class SmsRestController {
    private final ISmsHandler smsHandler;

    @PostMapping("/send")
    public ResponseEntity<SmsResponseDto> sendSms(@RequestBody SmsRequestDto request) {
        SmsResponseDto response = smsHandler.sendSms(request);
        return ResponseEntity.ok(response);
    }
}
