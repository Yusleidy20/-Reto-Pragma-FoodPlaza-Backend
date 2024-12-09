package com.example.foodplaza_users.domain.model;


import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {
    private Long userId;
    private String nameUser;
    private String lastname;
    private String docId;
    private String phoneNumber;
    private LocalDate birthDate;
    private String email;
    private String passwordUser;
    private UserRole userRole;

}
