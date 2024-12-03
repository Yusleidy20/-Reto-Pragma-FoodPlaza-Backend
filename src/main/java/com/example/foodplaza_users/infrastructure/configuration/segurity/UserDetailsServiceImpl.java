package com.example.foodplaza_users.infrastructure.configuration.segurity;

import com.example.foodplaza_users.domain.model.UserModel;
import com.example.foodplaza_users.infrastructure.out.jpa.entity.UserEntity;
import com.example.foodplaza_users.infrastructure.out.jpa.mapper.IUserEntityMapper;
import com.example.foodplaza_users.infrastructure.out.jpa.repository.IUserRepositoryMySQL;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final IUserRepositoryMySQL userRepository;
    private final IUserEntityMapper userEntityMapper;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("the user with email " + email));
        UserModel userModel = userEntityMapper.toUserModel(userEntity);
        return new UserDetailsImpl(userModel);
    }
}
