package com.example.foodplaza.application.handler;

import com.example.foodplaza.application.dto.request.DishRequestDto;
import com.example.foodplaza.application.dto.response.DishResponseDto;

public interface IDishHandlerPort {

    void createDish(DishRequestDto dishRequestDto);
    DishResponseDto getDishById(Long idDish);
}
