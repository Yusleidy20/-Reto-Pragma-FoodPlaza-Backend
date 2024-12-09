package com.example.foodplaza_traceability.infrastructure.out.jpa.feignclients.mapper;

import com.example.foodplaza_traceability.infrastructure.out.jpa.feignclients.RestaurantDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "restaurant-service", url = "http://localhost:9091/user-micro/foodplaza")
public interface IRestaurantFeignClient {
    @GetMapping("/restaurant/{idRestaurant}")
    RestaurantDto getRestaurantById(@PathVariable("idRestaurant") Long idRestaurant);


    @GetMapping("/restaurants")
    List<RestaurantDto> getRestaurants(@RequestParam int page, @RequestParam int size);

    @GetMapping("/restaurant/{idRestaurant}/orders")
    List<Long> getOrderIdsByRestaurantId(@PathVariable("idRestaurant") Long idRestaurant);
}
