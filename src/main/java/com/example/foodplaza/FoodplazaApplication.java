package com.example.foodplaza;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication(scanBasePackages = "com.example.foodplaza")
public class FoodplazaApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodplazaApplication.class, args);
	}

}
