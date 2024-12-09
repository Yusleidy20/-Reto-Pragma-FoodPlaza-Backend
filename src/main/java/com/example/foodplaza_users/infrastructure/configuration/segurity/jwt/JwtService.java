package com.example.foodplaza_users.infrastructure.configuration.segurity.jwt;

import com.example.foodplaza_users.infrastructure.configuration.segurity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

    public String generateToken(UserDetails userDetails) {
        Long userId = ((UserDetailsImpl) userDetails).getUserId();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        return JwtUtil.generateToken(userDetails.getUsername(), role, userId);
    }
}