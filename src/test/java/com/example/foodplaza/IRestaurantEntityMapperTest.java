package com.example.foodplaza;

import com.example.foodplaza.domain.model.RestaurantModel;
import com.example.foodplaza.infrastructure.out.jpa.entity.RestaurantEntity;
import com.example.foodplaza.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
 class IRestaurantEntityMapperTest {

    @Autowired
    private IRestaurantEntityMapper mapper;

    @Test
    void testToRestaurantEntity() {
        // Crear un modelo de prueba
        RestaurantModel model = RestaurantModel.builder()
                .idRestaurant(1L)
                .nameRestaurant("Test Restaurant")
                .nit("123456789")
                .address("123 Test St")
                .phoneNumber("+123456789")
                .urlLogo("http://example.com/logo.png")
                .ownerId(42L)
                .build();

        // Mapear a entidad
        RestaurantEntity entity = mapper.toRestaurantEntity(model);

        // Validar mapeo
        assertNotNull(entity);
        assertEquals(model.getIdRestaurant(), entity.getIdRestaurant());
        assertEquals(model.getNameRestaurant(), entity.getNameRestaurant());
        assertEquals(model.getNit(), entity.getNit());
        assertEquals(model.getAddress(), entity.getAddress());
        assertEquals(model.getPhoneNumber(), entity.getPhoneNumber());
        assertEquals(model.getUrlLogo(), entity.getUrlLogo());
        assertEquals(model.getOwnerId(), entity.getOwnerId());
    }

    @Test
    void testToRestaurantModel() {
        // Crear una entidad de prueba
        RestaurantEntity entity = new RestaurantEntity(
                1L, "Test Restaurant", "123456789", "123 Test St",
                "+123456789", "http://example.com/logo.png", 42L);

        // Mapear a modelo
        RestaurantModel model = mapper.toRestaurantModel(entity);

        // Validar mapeo
        assertNotNull(model);
        assertEquals(entity.getIdRestaurant(), model.getIdRestaurant());
        assertEquals(entity.getNameRestaurant(), model.getNameRestaurant());
        assertEquals(entity.getNit(), model.getNit());
        assertEquals(entity.getAddress(), model.getAddress());
        assertEquals(entity.getPhoneNumber(), model.getPhoneNumber());
        assertEquals(entity.getUrlLogo(), model.getUrlLogo());
        assertEquals(entity.getOwnerId(), model.getOwnerId());
    }
}
