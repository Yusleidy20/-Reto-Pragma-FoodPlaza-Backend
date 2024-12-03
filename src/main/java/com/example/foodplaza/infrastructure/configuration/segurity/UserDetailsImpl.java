package com.example.foodplaza_users.infrastructure.configuration.segurity;


import com.example.foodplaza_users.domain.model.UserModel;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private final UserModel userModel;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userModel.getUserRole().getNameRole()));
    }

    @Override
    public String getPassword() {
        return userModel.getPasswordUser();
    }

    @Override
    public String getUsername() {
        return userModel.getEmail();
    }

}
