package com.example.foodplaza.infrastructure.out.jpa.adapter;

import com.example.foodplaza.domain.exception.ResourceNotFoundException;
import com.example.foodplaza.domain.model.DishModel;
import com.example.foodplaza.domain.spi.persistence.IDishPersistencePort;
import com.example.foodplaza.infrastructure.out.jpa.entity.DishEntity;
import com.example.foodplaza.infrastructure.out.jpa.entity.RestaurantEntity;
import com.example.foodplaza.infrastructure.out.jpa.mapper.IDishEntityMapper;
import com.example.foodplaza.infrastructure.out.jpa.repository.IDishRepositoryMySql;
import com.example.foodplaza.infrastructure.out.jpa.repository.IRestaurantRepositoryMySql;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@RequiredArgsConstructor
public class DishAdapterImpl implements IDishPersistencePort {

    private final IDishRepositoryMySql dishRepository;
    private final IDishEntityMapper dishEntityMapper;
    private final IRestaurantRepositoryMySql restaurantRepository;
    private static final Logger log = LoggerFactory.getLogger(DishAdapterImpl.class);
    @Override
    @Transactional
    public DishModel saveDish(DishModel dishModel) {
        log.info("Guardando plato en la base de datos: {}", dishModel);

        // Validar que el restaurante existe
        RestaurantEntity restaurant = restaurantRepository.findById(dishModel.getIdRestaurant())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante no encontrado con ID: " + dishModel.getIdRestaurant()));

        // Mapear y asignar la entidad de restaurante
        DishEntity dishEntity = dishEntityMapper.toDishEntity(dishModel);
        dishEntity.setIdRestaurant(restaurant);

        // Guardar el plato
        DishEntity savedDishEntity = dishRepository.save(dishEntity);
        log.info("Plato guardado con ID: {}", savedDishEntity.getIdDish());

        return dishEntityMapper.toDishModel(savedDishEntity);
    }


    @Override
    public DishModel findDishById(Long dishId) {
        Optional<DishEntity> optionalDishEntity = dishRepository.findById(dishId);
        DishEntity dishEntity = optionalDishEntity.orElse(null);
        return dishEntityMapper.toDishModel(dishEntity);
    }

    @Override
    @Transactional
    public void updateDish(DishModel dishModel) {
        DishEntity dishEntity = dishEntityMapper.toDishEntity(dishModel);
        // Asignar el objeto RestaurantEntity completo
        RestaurantEntity restaurant = restaurantRepository.findById(dishModel.getIdRestaurant())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante no encontrado con ID: " + dishModel.getIdRestaurant()));
        dishEntity.setIdRestaurant(restaurant);
        dishRepository.save(dishEntity);
    }

    @Override
    @Transactional
    public void deleteDish(Long dishId) {
        dishRepository.deleteById(dishId);
    }

}

