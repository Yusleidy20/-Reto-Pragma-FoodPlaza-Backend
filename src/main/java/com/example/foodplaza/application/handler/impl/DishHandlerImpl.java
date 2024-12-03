package com.example.foodplaza.application.handler.impl;

import com.example.foodplaza.application.dto.request.DishRequestDto;
import com.example.foodplaza.application.dto.request.DishUpdateRequestDto;
import com.example.foodplaza.application.dto.response.DishResponseDto;
import com.example.foodplaza.application.handler.IDishHandlerPort;
import com.example.foodplaza.application.mapper.request.DishRequestMapper;
import com.example.foodplaza.application.mapper.response.DishResponseMapper;
import com.example.foodplaza.domain.api.IDishServicePort;
import com.example.foodplaza.domain.model.DishModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
@Transactional
public class DishHandlerImpl implements IDishHandlerPort {
    private final IDishServicePort dishServicePort;
    private final DishRequestMapper dishRequestMapper;
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

        // Si price no es nulo, actualiza el valor
        if (dishUpdateDto.getPrice() != null) {
            dishModel.setPrice(dishUpdateDto.getPrice());
        }
        // Si description no es nulo, actualiza el valor
        if (dishUpdateDto.getDescription() != null) {
            dishModel.setDescription(dishUpdateDto.getDescription());
        }
        // Si active no es nulo, actualiza el valor
        if (dishUpdateDto.getActive() != null) {
            dishModel.setActive(dishUpdateDto.getActive());
        }

        // Ahora se pasa el objeto dishModel al servicio
        dishServicePort.updateDish(dishModel);
    }





    @Override
    public DishResponseDto getDishById(Long idDish) {
        return dishResponseMapper.toDishResponse(dishServicePort.getDishById(idDish));
    }
}
