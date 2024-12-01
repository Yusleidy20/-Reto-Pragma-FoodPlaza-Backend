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
public class IRestaurantEntityMapperTest {

    @Autowired
    private IRestaurantEntityMapper mapper;

    @Test
    public void testMapperModelToEntity() {
        // Crear un modelo de prueba
        RestaurantModel model = new RestaurantModel();
        model.setIdRestaurant(1L);
        model.setNameRestaurant("Test Restaurant");
        model.setNit("123456789");
        model.setAddress("Cúcuta");
        model.setPhoneNumber("+1234567890");
        model.setUrlLogo("test-logo.png");
        model.setOwnerId(5L);

        // Convertir el modelo a entidad
        RestaurantEntity entity = mapper.toRestaurantEntity(model);

        // Validar los valores mapeados
        assertNotNull(entity);
        assertEquals(model.getNameRestaurant(), entity.getNameRestaurant());
        assertEquals(model.getAddress(), entity.getAddress());
        assertEquals(model.getOwnerId(), entity.getOwnerId());
    }
}