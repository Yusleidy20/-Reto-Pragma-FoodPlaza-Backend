package com.example.foodplaza.infrastructure.out.jpa.feignclients;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private Long userId;
    private String nameUser;
    private String lastname;
    private Long docId;
    private String phoneNumber;
    private LocalDate birthDate;
    private String email;
    private String passwordUser;
    private RoleDto userRole;
    public UserDto(Long userId, String nameUser, String lastname, Long docId, String phoneNumber) {
        this.userId = userId;
        this.nameUser = nameUser;
        this.lastname = lastname;
        this.docId = docId;
        this.phoneNumber = phoneNumber;
    }

}
