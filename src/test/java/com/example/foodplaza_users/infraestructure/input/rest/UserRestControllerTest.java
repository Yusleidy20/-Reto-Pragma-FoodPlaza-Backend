package com.example.foodplaza_users.infraestructure.input.rest;


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
import org.springframework.security.test.context.support.WithMockUser;

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
    @WithMockUser(roles = "Administrator") // Simula un usuario con rol de Administrador
    void testRegisterOwner_Success() {
        // Datos del DTO que se utilizarán para registrar el propietario
        UserRequestDto ownerDto = new UserRequestDto(
                "John",
                "Doe",
                "123456789",
                "+573005698325",
                LocalDate.of(1990, 1, 1),
                "owner@example.com",
                "password123",
                2L
        );

        // Simulamos que el servicio de manejo de usuarios (userServiceHandler) guarda el usuario correctamente
        Mockito.doNothing().when(userServiceHandler).saveUser(ownerDto);

        // Llamada al endpoint para registrar al propietario
        ResponseEntity<String> response = userRestController.registerOwner(ownerDto);

        // Verificamos que la respuesta sea un 201 (CREATED) y el mensaje esperado
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals("Owner registered successfully.", response.getBody());

        // Verificamos que el método saveUser se haya llamado con el ownerDto proporcionado
        Mockito.verify(userServiceHandler).saveUser(ownerDto);
    }




    /**
     * Test de fallo: Email inválido.
     */
    @Test
    void testRegisterOwner_Fail_InvalidEmail() {
        UserRequestDto ownerDto = new UserRequestDto(
                "John",
                "Doe",
                "123456789",
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
        UserRequestDto ownerDto = new UserRequestDto(
                "John",
                "Doe",
                "123456789",
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
        UserRequestDto ownerDto = new UserRequestDto(
                "John",
                "Doe",
                "123456789",
                "12345678901234", // Más de 13 caracteres
                LocalDate.of(1990, 1, 1),
                "owner@example.com",
                "password123",
                2L
        );



        Mockito.verify(userServiceHandler, Mockito.never()).saveUser(ownerDto);
    }



}







