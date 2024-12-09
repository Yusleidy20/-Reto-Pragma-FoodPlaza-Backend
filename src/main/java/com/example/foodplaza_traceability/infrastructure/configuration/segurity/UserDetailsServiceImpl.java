package com.example.foodplaza_traceability.infrastructure.configuration.segurity;


import com.example.foodplaza_traceability.infrastructure.out.jpa.feignclients.UserDto;
import com.example.foodplaza_traceability.infrastructure.out.jpa.feignclients.mapper.IUserFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final IUserFeignClient userFeignClient;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDto userDto = userFeignClient.getUserByEmail(email);
        return new UserDetailsImpl(userDto);
    }
}
