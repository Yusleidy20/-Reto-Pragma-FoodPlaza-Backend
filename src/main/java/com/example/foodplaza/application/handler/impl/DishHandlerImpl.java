package com.example.foodplaza.application.handler.impl;

import com.example.foodplaza.application.dto.request.DishRequestDto;
import com.example.foodplaza.application.dto.request.DishUpdateRequestDto;
import com.example.foodplaza.application.dto.response.DishListResponseDto;
import com.example.foodplaza.application.dto.response.DishResponseDto;
import com.example.foodplaza.application.handler.IDishHandlerPort;
import com.example.foodplaza.application.mapper.request.IDishRequestMapper;
import com.example.foodplaza.application.mapper.response.DishResponseMapper;
import com.example.foodplaza.domain.api.IDishServicePort;
import com.example.foodplaza.domain.model.DishModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class DishHandlerImpl implements IDishHandlerPort {

    private final IDishServicePort dishServicePort;
    private final IDishRequestMapper dishRequestMapper;
    private final DishResponseMapper dishResponseMapper;

    @Override
    public void createDish(DishRequestDto dishRequestDto) throws IllegalAccessException {
        DishModel dishModel = dishRequestMapper.toDishModel(dishRequestDto);
        dishServicePort.saveDish(dishModel);
    }

    @Override
    public void updateDish(Long idDish, DishUpdateRequestDto dishUpdateDto) {
        DishModel dishModel = new DishModel();
        dishModel.setIdDish(idDish);
        // Verificamos si al menos uno de los campos no es nulo, para actualizarlos
        if (dishUpdateDto.getPrice() != null || dishUpdateDto.getDescription() != null || dishUpdateDto.getActive() != null) {
            if (dishUpdateDto.getPrice() != null) {
                dishModel.setPrice(dishUpdateDto.getPrice());
            } else if (dishUpdateDto.getDescription() != null) {
                dishModel.setDescription(dishUpdateDto.getDescription());
            } else {
                dishModel.setActive(dishUpdateDto.getActive());
            }
        }
        dishServicePort.updateDish(dishModel);
    }

    @Override
    public DishResponseDto getDishById(Long idDish) {
        return dishResponseMapper.toDishResponse(dishServicePort.getDishById(idDish));
    }

    @Override
    public List<DishListResponseDto> listDishes(Pageable pageable, Long idCategory, Long idRestaurant) {
        Page<DishModel> dishes = dishServicePort.listDish(pageable, idCategory, idRestaurant);

        // Mapear los platos a DTO
        return dishes.map(dish -> new DishListResponseDto(
                dish.getNameDish(),
                dish.getPrice(),
                dish.getDescription(),
                dish.getUrlImage()
        )).getContent(); // Extrae solo la lista de contenido
    }


}

