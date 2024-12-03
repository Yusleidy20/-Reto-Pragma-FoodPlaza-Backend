package com.example.foodplaza.infrastructure.out.jpa.adapter;

import com.example.foodplaza.application.dto.response.RestaurantDto;
import com.example.foodplaza.domain.model.RestaurantModel;
import com.example.foodplaza.domain.spi.persistence.IRestaurantPersistencePort;
import com.example.foodplaza.infrastructure.out.jpa.entity.RestaurantEntity;
import com.example.foodplaza.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.example.foodplaza.infrastructure.out.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;


import java.util.Optional;

@RequiredArgsConstructor
public class RestaurantAdapterImpl implements IRestaurantPersistencePort {
    private final IRestaurantRepository restaurantRepositoryMySql;
    private  final IRestaurantEntityMapper restaurantEntityMapper;
    private static final Logger log = LoggerFactory.getLogger(RestaurantAdapterImpl.class);

    @Override
    public RestaurantModel saveRestaurant(RestaurantModel restaurantModel) {
        RestaurantEntity restaurantEntity = restaurantRepositoryMySql.save(restaurantEntityMapper.toRestaurantEntity(restaurantModel));
        log.info("Entity mapped before saving: {}", restaurantEntity);
        return restaurantEntityMapper.toRestaurantModel(restaurantEntity);
    }

    @Override
    public Page<RestaurantDto> getRestaurantsWithPaginationAndSorting(org.springframework.data.domain.Pageable pageable) {
        // Obtener las entidades desde el repositorio
        Page<RestaurantEntity> restaurantEntities = restaurantRepositoryMySql.findAll(pageable);

        // Mapear las entidades a DTOs con solo los campos necesarios
        return restaurantEntities.map(restaurantEntity -> {
            RestaurantDto dto = new RestaurantDto();
            dto.setNameRestaurant(restaurantEntity.getNameRestaurant());
            dto.setUrlLogo(restaurantEntity.getUrlLogo());
            return dto;
        });
    }



    @Override
    public RestaurantModel getRestaurantById(Long idRestaurant) {
        Optional<RestaurantEntity> optionalRestaurantEntity = restaurantRepositoryMySql.findById(idRestaurant);
        RestaurantEntity restaurantEntity = optionalRestaurantEntity.orElse(null);
        return restaurantEntityMapper.toRestaurantModel(restaurantEntity);
    }

    @Override
    public RestaurantModel getRestaurantByIdOwner(Long ownerId) {
        Optional<RestaurantEntity> optionalRestaurantEntity = restaurantRepositoryMySql.findByOwnerId(ownerId);
        RestaurantEntity restaurantEntity = optionalRestaurantEntity.orElse(null);
        return restaurantEntityMapper.toRestaurantModel(restaurantEntity);
    }




}

