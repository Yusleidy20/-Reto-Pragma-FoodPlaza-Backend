package com.example.foodplaza.application.handler.impl;

import com.example.foodplaza.application.dto.request.RestaurantRequestDto;
import com.example.foodplaza.application.dto.response.RestaurantDto;
import com.example.foodplaza.application.dto.response.RestaurantResponseDto;
import com.example.foodplaza.application.handler.IRestaurantHandlerPort;
import com.example.foodplaza.application.mapper.request.IRestaurantRequestMapper;
import com.example.foodplaza.application.mapper.response.IRestaurantResponseMapper;
import com.example.foodplaza.domain.api.IRestaurantServicePort;
import com.example.foodplaza.domain.model.RestaurantModel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantHandlerImpl implements IRestaurantHandlerPort {
    private final IRestaurantServicePort restaurantServicePort;

    private final IRestaurantRequestMapper restaurantRequestMapper;

    private final IRestaurantResponseMapper restaurantResponseMapper;
    private static final Logger log = LoggerFactory.getLogger(RestaurantHandlerImpl.class);


    @Override
    public void saveRestaurant(RestaurantRequestDto restaurantRequestDto) {
        RestaurantModel restaurantModel = restaurantRequestMapper.toRestaurant(restaurantRequestDto);
        restaurantServicePort.saveRestaurant(restaurantModel);
    }
    @Override
    public Page<RestaurantDto> getRestaurants(int page, int size) {
        // Obtener la página de restaurantes con la paginación y el ordenamiento
        Page<RestaurantDto> restaurantsPage = restaurantServicePort.getRestaurantsWithPaginationAndSorting(page, size, "nameRestaurant");

        // Mapear la página para devolver solo nameRestaurant y urlLogo
        return restaurantsPage.map(restaurant -> {
            // Crear un RestaurantResponseDto con solo los campos necesarios
            RestaurantDto dto = new RestaurantDto();
            dto.setNameRestaurant(restaurant.getNameRestaurant());
            dto.setUrlLogo(restaurant.getUrlLogo());

            // Solo estamos mapeando nameRestaurant y urlLogo
            return dto;
        });
    }




    @Override
    public RestaurantResponseDto getRestaurantById(Long idRestaurant) {
        return restaurantResponseMapper.toRestaurantResponse(restaurantServicePort.getRestaurantById(idRestaurant));
    }

    @Override
    public RestaurantResponseDto getRestaurantByIdOwner(Long ownerId) {
        return restaurantResponseMapper.toRestaurantResponse(restaurantServicePort.getRestaurantByIdOwner(ownerId));
    }

    @Override
    public List<RestaurantResponseDto> getAllRestaurants() {
        return restaurantResponseMapper.toResponseList(restaurantServicePort.getAllRestaurants());
    }

    @Override
    public void deleteRestaurantById(Long idRestaurant) {
        restaurantServicePort.deleteRestaurantById(idRestaurant);
    }
}