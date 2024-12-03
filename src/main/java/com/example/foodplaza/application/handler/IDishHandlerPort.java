package com.example.foodplaza.application.handler;

import com.example.foodplaza.application.dto.request.DishRequestDto;
import com.example.foodplaza.application.dto.request.DishUpdateRequestDto;
import com.example.foodplaza.application.dto.response.DishResponseDto;

import jakarta.validation.Valid;


public interface IDishHandlerPort {

    void createDish(DishRequestDto dishRequestDto) throws IllegalAccessException;
    void updateDish(Long idDish, @Valid DishUpdateRequestDto dishRequestDto);
    DishResponseDto getDishById(Long idDish);

}
