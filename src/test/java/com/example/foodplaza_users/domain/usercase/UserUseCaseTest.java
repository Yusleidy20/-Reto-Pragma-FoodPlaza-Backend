package com.example.foodplaza_users.domain.usercase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.foodplaza_users.domain.model.UserModel;
import com.example.foodplaza_users.domain.model.UserRole;
import com.example.foodplaza_users.domain.spi.persistence.IUserPersistencePort;
import com.example.foodplaza_users.domain.usecase.UserUseCase;
import com.example.foodplaza_users.infrastructure.configuration.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

class UserUseCaseTest {

    @Mock
    private IUserPersistencePort userPersistencePort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserUseCase userUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveUser_ValidUser() {
        // Arrange
        UserModel userModel = new UserModel();
        userModel.setNameUser("John");
        userModel.setLastname("Doe");
        userModel.setBirthDate(LocalDate.of(2000, 1, 1));
        userModel.setPasswordUser("password123");
        userModel.setUserRole(new UserRole(Constants.OWNER_ROLE_ID, Constants.ROLE_OWNER, "Owner"));

        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        // Act
        userUseCase.saveUser(userModel);

        // Assert
        verify(userPersistencePort, times(1)).saveUser(userModel);
        assertEquals("encodedPassword", userModel.getPasswordUser());
        assertEquals(Constants.OWNER_ROLE_ID, userModel.getUserRole().getIdUserRole());
    }

    @Test
    void testSaveUser_InvalidBirthDate() {
        // Arrange
        UserModel userModel = new UserModel();
        userModel.setBirthDate(LocalDate.now().plusDays(1)); // Fecha en el futuro

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userUseCase.saveUser(userModel);
        });

        assertEquals("The date of birth cannot be in the future.", exception.getMessage());
        verify(userPersistencePort, never()).saveUser(any());
    }

    @Test
    void testSaveUser_NotAdult() {
        // Arrange
        UserModel userModel = new UserModel();
        userModel.setBirthDate(LocalDate.now().minusYears(16)); // Menor de edad

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userUseCase.saveUser(userModel);
        });

        assertEquals("The user must be of legal age.", exception.getMessage());
        verify(userPersistencePort, never()).saveUser(any());
    }


    @Test
    void testFindById_UserExists() {
        // Arrange
        UserModel mockUser = new UserModel();
        mockUser.setNameUser("John");

        when(userPersistencePort.findById(1L)).thenReturn(mockUser);

        // Act
        UserModel user = userUseCase.findById(1L);

        // Assert
        assertNotNull(user);
        assertEquals("John", user.getNameUser());
        verify(userPersistencePort, times(1)).findById(1L);
    }

    @Test
    void testFindById_UserNotFound() {
        // Arrange
        when(userPersistencePort.findById(1L)).thenReturn(null);

        // Act
        UserModel user = userUseCase.findById(1L);

        // Assert
        assertNull(user);
        verify(userPersistencePort, times(1)).findById(1L);
    }
}
