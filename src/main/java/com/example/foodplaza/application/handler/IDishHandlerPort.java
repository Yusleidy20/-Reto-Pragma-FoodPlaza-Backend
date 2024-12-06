package com.example.foodplaza.application.handler;

import com.example.foodplaza.application.dto.request.DishRequestDto;
import com.example.foodplaza.application.dto.request.DishUpdateRequestDto;
import com.example.foodplaza.application.dto.response.DishListResponseDto;
import com.example.foodplaza.application.dto.response.DishResponseDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDishHandlerPort {

    void createDish(DishRequestDto dishRequestDto) throws IllegalAccessException;
    void updateDish(Long idDish, @Valid DishUpdateRequestDto dishRequestDto);
    DishResponseDto getDishById(Long idDish);
    List<DishListResponseDto> listDishes(Pageable pageable, Long idCategory, Long idRestaurant);  // Aquí cambiamos idCategoria a Long

}
