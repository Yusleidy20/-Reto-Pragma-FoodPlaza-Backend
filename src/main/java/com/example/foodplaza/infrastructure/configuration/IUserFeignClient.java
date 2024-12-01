package com.example.foodplaza.infrastructure.configuration;

import com.example.foodplaza.infrastructure.out.jpa.feignclients.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "user-service", url = "http://localhost:8095")
public interface IUserFeignClient {
    @GetMapping("/user-micro/user/existUserById/{userId}")
    boolean existUserById(@PathVariable("userId") Long userId);

    @GetMapping("/user-micro/user/{userId}")
    UserDto getUserById(@PathVariable("userId") Long userId);

    @GetMapping("/user-micro/user/email/{email}")
    UserDto getUserByEmail(@PathVariable("email") String email);

}
