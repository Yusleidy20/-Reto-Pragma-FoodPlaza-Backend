package com.example.foodplaza.infraestructure.input.rest;

import com.example.foodplaza.application.dto.request.DishRequestDto;
import com.example.foodplaza.application.dto.request.DishUpdateRequestDto;
import com.example.foodplaza.application.dto.response.DishListResponseDto;
import com.example.foodplaza.application.handler.IDishHandlerPort;
import com.example.foodplaza.infrastructure.input.rest.DishRestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.data.domain.PageRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DishRestController.class)
@ExtendWith(MockitoExtension.class)
class DishRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IDishHandlerPort dishHandlerPort;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    /**
     * Prueba para verificar que un plato se crea correctamente.
     */
    @Test
    @WithMockUser(authorities = "Owner")
    void createDish_shouldCreateDish_whenRequestIsValid() throws Exception {
        // Crear un DishRequestDto válido
        DishRequestDto dishRequestDto = new DishRequestDto("Pizza", 200, "Delicious pizza", "http://image.com/pizza.png", 1L, 1L);

        // Acción y verificación
        mockMvc.perform(post("/user-micro/foodplaza/dish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dishRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Dish created successfully."));

        // Verificar que el handler fue invocado
        verify(dishHandlerPort).createDish(dishRequestDto);
    }

    /**
     * Prueba para verificar que se lanza un error si el usuario no tiene la autoridad requerida.
     */
    @Test
    void createDish_shouldReturnForbidden_whenUserIsNotOwner() throws Exception {
        // Crear un DishRequestDto válido
        DishRequestDto dishRequestDto = new DishRequestDto("Pizza", 200, "Delicious pizza", "http://image.com/pizza.png", 1L, 1L);

        // Acción y verificación
        mockMvc.perform(post("/user-micro/foodplaza/dish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dishRequestDto)))
                .andExpect(status().isForbidden());

        // Verificar que el handler no fue invocado
        verify(dishHandlerPort, never()).createDish(any());
    }

    /**
     * Prueba para verificar que un plato se actualiza correctamente.
     */
    @Test
    @WithMockUser(authorities = "Owner")
    void updateDish_shouldUpdateDish_whenRequestIsValid() throws Exception {
        // Crear un DishUpdateRequestDto válido
        DishUpdateRequestDto dishUpdateDto = new DishUpdateRequestDto(250, "Updated description", true);

        // Acción y verificación
        mockMvc.perform(put("/user-micro/foodplaza/dish/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dishUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Dish updated successfully."));

        // Verificar que el handler fue invocado
        verify(dishHandlerPort).updateDish(1L, dishUpdateDto);

    }

    /**
     * Prueba para verificar que se lista correctamente los platos.
     */
    @Test
    @WithMockUser(roles = "Customer")
    void listDishes_shouldReturnDishList_whenRequestIsValid() throws Exception {
        // Crear una lista de respuesta simulada
        List<DishListResponseDto> dishList = List.of(
                new DishListResponseDto("Pizza", 100, "Delicious pizza", "http://image.com/pizza.png"),
                new DishListResponseDto("Burger", 150, "Tasty burger", "http://image.com/burger.png")

        );

        // Configuración del mock
        when(dishHandlerPort.listDishes(any(PageRequest.class), eq(1L), eq(1L)))
                .thenReturn(dishList);

        // Acción y verificación
        mockMvc.perform(get("/user-micro/foodplaza/listDish")
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .param("idCategory", "1")
                        .param("idRestaurant", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Pizza"))
                .andExpect(jsonPath("$[1].name").value("Burger"));

        // Verificar que el handler fue invocado
        verify(dishHandlerPort).listDishes(any(PageRequest.class), eq(1L), eq(1L));
    }
}
