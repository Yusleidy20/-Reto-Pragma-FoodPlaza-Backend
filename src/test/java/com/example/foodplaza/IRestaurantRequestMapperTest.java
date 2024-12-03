package com.example.foodplaza;

import com.example.foodplaza.application.dto.request.RestaurantRequestDto;
import com.example.foodplaza.application.mapper.request.IRestaurantRequestMapper;
import com.example.foodplaza.domain.model.RestaurantModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class IRestaurantRequestMapperTest {

    @Autowired
    private IRestaurantRequestMapper mapper;

    @Test
    void testToRestaurant() {
        RestaurantRequestDto dto = new RestaurantRequestDto();

        RestaurantModel model = mapper.toRestaurant(dto);

        assertNotNull(model);
        assertEquals(dto.getIdRestaurant(), model.getIdRestaurant());
        assertEquals(dto.getNameRestaurant(), model.getNameRestaurant());
        assertEquals(dto.getNit(), model.getNit());
        assertEquals(dto.getAddress(), model.getAddress());
        assertEquals(dto.getPhoneNumber(), model.getPhoneNumber());
        assertEquals(dto.getUrlLogo(), model.getUrlLogo());
        assertEquals(dto.getOwnerId(), model.getOwnerId());
    }

    @Test
    void testToRestaurantRequest() {
        RestaurantModel model = RestaurantModel.builder()
                .idRestaurant(1L)
                .nameRestaurant("Test Restaurant")
                .nit("123456")
                .address("Test Address")
                .phoneNumber("123456789")
                .urlLogo("http://example.com/logo.png")
                .ownerId(10L)
                .build();

        RestaurantRequestDto dto = mapper.toRestaurantRequest(model);

        assertNotNull(dto);
        assertEquals(model.getIdRestaurant(), dto.getIdRestaurant());
        assertEquals(model.getNameRestaurant(), dto.getNameRestaurant());
        assertEquals(model.getNit(), dto.getNit());
        assertEquals(model.getAddress(), dto.getAddress());
        assertEquals(model.getPhoneNumber(), dto.getPhoneNumber());
        assertEquals(model.getUrlLogo(), dto.getUrlLogo());
        assertEquals(model.getOwnerId(), dto.getOwnerId());
    }
}
