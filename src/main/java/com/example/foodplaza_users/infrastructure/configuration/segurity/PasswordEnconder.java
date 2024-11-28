package com.example.foodplaza_users.infrastructure.configuration.segurity;

import com.example.foodplaza_users.domain.spi.IUserPasswordEncoderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class PasswordEnconder implements IUserPasswordEncoderPort {
    private final PasswordEncoder passwordEncoder =  new BCryptPasswordEncoder();

    @Override
    public String encode(String password) {
        return passwordEncoder.encode(password);
    }
}
