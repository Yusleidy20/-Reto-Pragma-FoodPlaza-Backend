package com.example.foodplaza_users.infraestructure.input.rest;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.example.foodplaza_users.application.handler.IRoleServiceHandler;
import com.example.foodplaza_users.application.handler.IUserServiceHandler;
import com.example.foodplaza_users.domain.model.UserModel;
import com.example.foodplaza_users.domain.model.UserRole;
import com.example.foodplaza_users.infrastructure.input.rest.UserRestController;

import java.time.LocalDate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

 class UserRestControllerTest {
    @Mock
    private IUserServiceHandler userServiceHandler;

    @Mock
    private IRoleServiceHandler roleServiceHandler;

    @InjectMocks
    private UserRestController userRestController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userRestController).build();
        objectMapper = new ObjectMapper();
    }






}
