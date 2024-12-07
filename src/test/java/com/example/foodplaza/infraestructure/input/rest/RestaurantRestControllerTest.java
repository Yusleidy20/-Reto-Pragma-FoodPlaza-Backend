package com.example.foodplaza.infraestructure.input.rest;

import com.example.foodplaza.TestSecurityConfig;
import com.example.foodplaza.application.dto.request.RestaurantRequestDto;
import com.example.foodplaza.application.dto.response.RestaurantDto;
import com.example.foodplaza.application.handler.IRestaurantHandlerPort;
import com.example.foodplaza.infrastructure.input.rest.RestaurantRestController;
import com.example.foodplaza.infrastructure.out.jpa.feignclients.mapper.IRoleFeignClient;
import com.example.foodplaza.infrastructure.out.jpa.feignclients.mapper.IUserFeignClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestaurantRestController.class)
@Import(TestSecurityConfig.class)
class RestaurantRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IRestaurantHandlerPort restaurantHandlerPort;

    @MockBean
    private IUserFeignClient userFeignClient;

    @MockBean
    private IRoleFeignClient roleFeignClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(authorities = "Administrator")
    void createRestaurant_ShouldReturnCreatedResponse_WhenRequestIsValid() throws Exception {
        // Arrange
        RestaurantRequestDto restaurantRequestDto = new RestaurantRequestDto(
                null,
                "Pizza Place",
                "123456",
                "Main Street",
                "+123456789",
                "http://logo.com",
                1L
        );

        // Act & Assert
        mockMvc.perform(post("/user-micro/foodplaza/restaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurantRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Restaurant created successfully."));

        // Verifica que el mock reciba un objeto equivalente al esperado
        Mockito.verify(restaurantHandlerPort).saveRestaurant(Mockito.argThat(argument ->
                argument.getNameRestaurant().equals(restaurantRequestDto.getNameRestaurant()) &&
                        argument.getNit().equals(restaurantRequestDto.getNit()) &&
                        argument.getAddress().equals(restaurantRequestDto.getAddress()) &&
                        argument.getPhoneNumber().equals(restaurantRequestDto.getPhoneNumber()) &&
                        argument.getUrlLogo().equals(restaurantRequestDto.getUrlLogo()) &&
                        argument.getOwnerId().equals(restaurantRequestDto.getOwnerId())
        ));
    }


    @Test
    @WithMockUser(authorities = "Customer")
    void getRestaurants_ShouldReturnPaginatedRestaurants_WhenRequestIsValid() throws Exception {
        // Arrange
        int page = 0;
        int size = 10;
        List<RestaurantDto> mockRestaurants = List.of(
                new RestaurantDto("Pizza Place", "http://logo1.com"),
                new RestaurantDto("Burger Place", "http://logo2.com")
        );

        Page<RestaurantDto> mockPage = new PageImpl<>(mockRestaurants);

        Mockito.when(restaurantHandlerPort.getRestaurants(page, size)).thenReturn(mockPage);

        // Act & Assert
        mockMvc.perform(get("/user-micro/foodplaza/restaurants")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nameRestaurant", is("Pizza Place")))
                .andExpect(jsonPath("$[1].nameRestaurant", is("Burger Place")));

        Mockito.verify(restaurantHandlerPort).getRestaurants(page, size);
    }

    @Test
    @WithMockUser(authorities = "Customer")
    void getRestaurants_ShouldReturnEmptyList_WhenNoRestaurantsAvailable() throws Exception {
        // Arrange
        int page = 0;
        int size = 10;
        Page<RestaurantDto> emptyPage = new PageImpl<>(List.of());

        Mockito.when(restaurantHandlerPort.getRestaurants(page, size)).thenReturn(emptyPage);

        // Act & Assert
        mockMvc.perform(get("/user-micro/foodplaza/restaurants")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        Mockito.verify(restaurantHandlerPort).getRestaurants(page, size);
    }
}
