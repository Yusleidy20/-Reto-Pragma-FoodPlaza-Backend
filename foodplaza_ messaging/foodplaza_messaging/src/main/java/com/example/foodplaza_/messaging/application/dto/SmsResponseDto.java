package com.example.foodplaza_.messaging.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SmsResponseDto {
    private String phoneNumber;
    private String message;
    private String status;
    private String messageId;
}
