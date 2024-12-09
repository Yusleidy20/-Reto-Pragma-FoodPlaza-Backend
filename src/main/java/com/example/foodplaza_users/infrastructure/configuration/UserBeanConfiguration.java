package com.example.foodplaza_users.infrastructure.configuration;

import com.example.foodplaza_users.domain.api.IRoleServicePort;
import com.example.foodplaza_users.domain.api.IUserServicePort;

import com.example.foodplaza_users.domain.spi.persistence.IUserRolePersistencePort;
import com.example.foodplaza_users.domain.usecase.RoleUseCase;
import com.example.foodplaza_users.domain.usecase.UserUseCase;
import com.example.foodplaza_users.infrastructure.out.jpa.adapter.RolePersistenceAdapterPortImpl;
import com.example.foodplaza_users.infrastructure.out.jpa.adapter.UserPersistenceAdapterPortImpl;
import com.example.foodplaza_users.infrastructure.out.jpa.mapper.IRoleEntityMapper;
import com.example.foodplaza_users.infrastructure.out.jpa.mapper.IUserEntityMapper;
import com.example.foodplaza_users.infrastructure.out.jpa.repository.IUserRepositoryMySQL;
import com.example.foodplaza_users.infrastructure.out.jpa.repository.IUserRoleRepositoryMySQL;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@RequiredArgsConstructor
@Configuration
public class UserBeanConfiguration {

    private final IUserRepositoryMySQL userRepositoryMySQL;
    private final IUserEntityMapper userEntityMapper;
    private final IUserRoleRepositoryMySQL roleRepositoryMySQL;
    private final IRoleEntityMapper roleEntityMapper;

    @Bean
    public UserPersistenceAdapterPortImpl userPersistenceAdapterPort() {
        return new UserPersistenceAdapterPortImpl(userRepositoryMySQL, userEntityMapper);
    }

    @Bean
    public IUserRolePersistencePort rolesPersistencePort() {
        return new RolePersistenceAdapterPortImpl(roleRepositoryMySQL, roleEntityMapper);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public IUserServicePort userUseCasePort() {
        return new UserUseCase(userPersistenceAdapterPort(),passwordEncoder());
    }

    @Bean
    public IRoleServicePort roleServicePort() {
        return new RoleUseCase(rolesPersistencePort());
    }


    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }


}
