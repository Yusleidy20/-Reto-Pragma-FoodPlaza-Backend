package com.example.foodplaza.application.dto.response;



import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Setter
@Getter
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
