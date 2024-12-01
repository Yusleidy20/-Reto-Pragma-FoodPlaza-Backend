package com.example.foodplaza.application.handler.impl;

import com.example.foodplaza.application.dto.request.RestaurantRequestDto;
import com.example.foodplaza.application.dto.response.RestaurantResponseDto;
import com.example.foodplaza.application.handler.IRestaurantHandlerPort;
import com.example.foodplaza.application.mapper.IRestaurantRequestMapper;
import com.example.foodplaza.application.mapper.IRestaurantResponseMapper;
import com.example.foodplaza.domain.api.IRestaurantServicePort;
import com.example.foodplaza.domain.model.RestaurantModel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        log.info("Mapper: Mapeando RestaurantRequestDto a RestaurantModel...");
        RestaurantModel restaurantModel = restaurantRequestMapper.toRestaurant(restaurantRequestDto);
        log.info("Resultado del mapper: {}", restaurantModel);
        restaurantServicePort.saveRestaurant(restaurantModel);
    }

    @Override
    public RestaurantResponseDto getRestaurantById(Long idRestaurant) {
        RestaurantResponseDto restaurantResponseDto = restaurantResponseMapper.toRestaurantResponse(restaurantServicePort.getRestaurantById(idRestaurant));
        return restaurantResponseDto;
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