package com.example.foodplaza.infrastructure.configuration.segurity;




import com.example.foodplaza.infrastructure.out.jpa.feignclients.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private final UserDto userDto;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName = (userDto.getUserRole() != null && userDto.getUserRole().getNameRole() != null)
                ? userDto.getUserRole().getNameRole()
                : "Default";

        // Aquí puedes lanzar una excepción si necesitas garantizar que no sea 'Default'
        if ("Default".equals(roleName)) {
            throw new IllegalStateException("User role is missing. Cannot proceed with authentication.");
        }

        return List.of(new SimpleGrantedAuthority(roleName));
    }


    @Override
    public String getPassword() {
        return userDto.getPasswordUser();
    }

    @Override
    public String getUsername() {
        return userDto.getEmail();
    }

}
