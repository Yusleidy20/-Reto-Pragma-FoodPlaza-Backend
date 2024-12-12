package com.example.foodplaza_.messaging.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SmsModel {
    private String phoneNumber;
    private String message;
    private String status;
    private String messageId;
}
