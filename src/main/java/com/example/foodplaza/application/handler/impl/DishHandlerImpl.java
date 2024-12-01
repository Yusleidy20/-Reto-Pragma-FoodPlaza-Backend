package com.example.foodplaza.application.handler.impl;

import com.example.foodplaza.application.dto.request.DishRequestDto;
import com.example.foodplaza.application.dto.request.DishUpdateRequestDto;
import com.example.foodplaza.application.dto.response.DishResponseDto;
import com.example.foodplaza.application.handler.IDishHandlerPort;
import com.example.foodplaza.application.mapper.request.DishRequestMapper;
import com.example.foodplaza.application.mapper.response.DishResponseMapper;
import com.example.foodplaza.domain.api.IDishServicePort;
import com.example.foodplaza.domain.model.DishModel;
import com.example.foodplaza.infrastructure.out.jpa.repository.IRestaurantRepositoryMySql;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DishHandlerImpl implements IDishHandlerPort {
    private final IDishServicePort dishServicePort;
    private final IRestaurantRepositoryMySql repositoryMySql;
    private final DishRequestMapper dishRequestMapper;
    private final DishResponseMapper dishResponseMapper;
    private static final Logger log = LoggerFactory.getLogger(DishHandlerImpl.class);



    @Override
    public void createDish(DishRequestDto dishRequestDto) {
        DishModel dishModel = dishRequestMapper.toDishModel(dishRequestDto);
        dishServicePort.saveDish(dishModel);
    }
    @Override
    public void updateDish(Long idDish, DishUpdateRequestDto dishUpdateDto) {

        // Mapea el DTO a un modelo
        DishModel dishModel = new DishModel();
        dishModel.setIdDish(idDish);
        dishModel.setPrice(dishUpdateDto.getPrice());
        dishModel.setDescription(dishUpdateDto.getDescription());

        // Llamar al caso de uso para actualizar
        dishServicePort.updateDish(dishModel);
    }

    @Override
    public DishResponseDto getDishById(Long idDish) {
        return dishResponseMapper.toDishResponse(dishServicePort.getDishById(idDish));
    }
}
