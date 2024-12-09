package com.example.foodplaza_traceability.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserResponseDto {
    private String nameUser;
    private String lastname;
    private Long docId;
    private String phoneNumber;
    private LocalDate birthDate;
    private String email;
    private String passwordUser;
    private Long idUserRole;

}
