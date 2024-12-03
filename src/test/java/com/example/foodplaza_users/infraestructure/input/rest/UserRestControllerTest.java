package com.example.foodplaza_users.infraestructure.input.rest;


import com.example.foodplaza_users.application.dto.response.UserResponseDto;
import com.example.foodplaza_users.application.dto.resquest.UserRequestDto;
import com.example.foodplaza_users.application.handler.IRoleServiceHandler;
import com.example.foodplaza_users.application.handler.IUserServiceHandler;
import com.example.foodplaza_users.domain.exception.UserRoleNotFountException;
import com.example.foodplaza_users.infrastructure.input.rest.UserRestController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class UserRestControllerTest {

    @InjectMocks
    private UserRestController userRestController;

    @Mock
    private IUserServiceHandler userServiceHandler;

    @Mock
    private IRoleServiceHandler roleServiceHandler;

    /**
     * Test de éxito: Registro de propietario correctamente.
     */
    @Test
    void testRegisterOwner_Success() {
        Long adminId = 1L;
        UserRequestDto ownerDto = new UserRequestDto(
                "John",
                "Doe",
                123456789L,
                "+573005698325",
                LocalDate.of(1990, 1, 1),
                "owner@example.com",
                "password123",
                2L
        );

        Mockito.when(userServiceHandler.isAdmin(adminId)).thenReturn(true);

        ResponseEntity<String> response = userRestController.registerOwner(ownerDto);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals("Owner registered successfully.", response.getBody());
        Mockito.verify(userServiceHandler).saveUser(ownerDto);
    }

    /**
     * Test de fallo: Usuario no tiene rol de administrador.
     */
    @Test
    void testRegisterOwner_Fail_NotAdmin() {
        Long adminId = 2L;
        UserRequestDto ownerDto = new UserRequestDto(
                "John",
                "Doe",
                123456789L,
                "+573005698325",
                LocalDate.of(1990, 1, 1),
                "owner@example.com",
                "password123",
                2L
        );

        Mockito.when(userServiceHandler.isAdmin(adminId)).thenReturn(false);

        UserRoleNotFountException exception = Assertions.assertThrows(
                UserRoleNotFountException.class,
                () -> userRestController.registerOwner(ownerDto)
        );

        Assertions.assertEquals("User must have Administrator role.", exception.getMessage());
        Mockito.verify(userServiceHandler, Mockito.never()).saveUser(ownerDto);
    }

    /**
     * Test de fallo: Email inválido.
     */
    @Test
    void testRegisterOwner_Fail_InvalidEmail() {
        Long adminId = 1L;
        UserRequestDto ownerDto = new UserRequestDto(
                "John",
                "Doe",
                123456789L,
                "+573005698325",
                LocalDate.of(1990, 1, 1),
                "invalid-email",
                "password123",
                2L
        );


        Mockito.verify(userServiceHandler, Mockito.never()).saveUser(ownerDto);
    }

    /**
     * Test de fallo: Usuario menor de edad.
     */
    @Test
    void testRegisterOwner_Fail_NotAdult() {
        Long adminId = 1L;
        UserRequestDto ownerDto = new UserRequestDto(
                "John",
                "Doe",
                123456789L,
                "+573005698325",
                LocalDate.now().minusYears(17),
                "owner@example.com",
                "password123",
                2L
        );


        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userRestController.registerOwner(ownerDto)
        );

        Assertions.assertEquals("User must be an adult.", exception.getMessage());
        Mockito.verify(userServiceHandler, Mockito.never()).saveUser(ownerDto);
    }

    /**
     * Test de fallo: Teléfono inválido.
     */
    @Test
    void testRegisterOwner_Fail_InvalidPhone() {
        Long adminId = 1L;
        UserRequestDto ownerDto = new UserRequestDto(
                "John",
                "Doe",
                123456789L,
                "12345678901234", // Más de 13 caracteres
                LocalDate.of(1990, 1, 1),
                "owner@example.com",
                "password123",
                2L
        );



        Mockito.verify(userServiceHandler, Mockito.never()).saveUser(ownerDto);
    }



}







