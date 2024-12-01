package com.example.foodplaza.application.handler.impl;

import com.example.foodplaza.application.dto.request.DishRequestDto;
import com.example.foodplaza.application.dto.response.DishResponseDto;
import com.example.foodplaza.application.handler.IDishHandlerPort;
import com.example.foodplaza.application.mapper.request.DishRequestMapper;
import com.example.foodplaza.application.mapper.response.DishResponseMapper;
import com.example.foodplaza.domain.api.IDishServicePort;
import com.example.foodplaza.domain.model.DishModel;
import com.example.foodplaza.infrastructure.out.jpa.repository.IRestaurantRepositoryMySql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service

public class DishHandlerImpl implements IDishHandlerPort {
    private final IDishServicePort dishServicePort;
    private final IRestaurantRepositoryMySql repositoryMySql;
    private final DishRequestMapper dishRequestMapper;
    private final DishResponseMapper dishResponseMapper;
    private static final Logger log = LoggerFactory.getLogger(DishHandlerImpl.class);

    public DishHandlerImpl(IDishServicePort dishServicePort, IRestaurantRepositoryMySql repositoryMySql, DishRequestMapper dishRequestMapper, DishResponseMapper dishResponseMapper) {
        this.dishServicePort = dishServicePort;
        this.repositoryMySql = repositoryMySql;
        this.dishRequestMapper = dishRequestMapper;
        this.dishResponseMapper = dishResponseMapper;
        log.info("DishHandlerImpl instanciado correctamente.");
    }

    @Override
    public void createDish(DishRequestDto dishRequestDto) {
        log.info("Datos recibidos en DishRequestDto: {}", dishRequestDto);
        DishModel dishModel = dishRequestMapper.toDishModel(dishRequestDto);
        log.info("Datos mapeados a DishModel: {}", dishModel);
        dishServicePort.saveDish(dishModel);
    }


    @Override
    public DishResponseDto getDishById(Long idDish) {
        return dishResponseMapper.toDishResponse(dishServicePort.getDishById(idDish));
    }
}
