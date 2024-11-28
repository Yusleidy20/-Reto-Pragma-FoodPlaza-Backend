package com.example.foodplaza_users.application.dto.resquest;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    @NotEmpty(message = "Name is required")
    private String nameUser;

    @NotEmpty(message = "Last name is required")
    private String lastname;
    @NotBlank(message = "The Identity document is required")
    @Pattern(regexp = "\\d+", message = "The Identity Document must be numeric")
    private Long docId;

    @NotEmpty(message = "Phone number is required")
    @Pattern(regexp = "^\\+?\\d{1,12}$", message = "The cell phone number must contain a maximum of 13 characters and may contain the '+' symbol at the beginning")
    private String phoneNumber;

    @NotNull(message = "Birthdate is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @NotEmpty(message = "Email is required")
    private String email;

    @NotEmpty(message = "Password is required")
    private String passwordUser;

    private Long idUserRole;
}