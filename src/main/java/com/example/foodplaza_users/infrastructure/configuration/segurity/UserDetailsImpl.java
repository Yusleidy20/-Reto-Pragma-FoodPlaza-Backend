package com.example.foodplaza_users.infrastructure.configuration.segurity;


import com.example.foodplaza_users.domain.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private UserModel userModel;

    public Long getUserId() {
        return userModel.getUserId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Retorna el rol del usuario
        return List.of(new SimpleGrantedAuthority(userModel.getUserRole().getNameRole()));
    }

    @Override
    public String getUsername() {
        return userModel.getEmail();
    }

    @Override
    public String getPassword() {
        return userModel.getPasswordUser();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

