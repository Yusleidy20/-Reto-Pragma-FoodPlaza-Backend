package com.example.foodplaza_traceability;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class FoodplazaTraceabilityApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodplazaTraceabilityApplication.class, args);
	}

}
