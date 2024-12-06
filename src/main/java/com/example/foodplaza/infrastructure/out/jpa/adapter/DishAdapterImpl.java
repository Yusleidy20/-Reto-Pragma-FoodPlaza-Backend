package com.example.foodplaza.infrastructure.out.jpa.adapter;

import com.example.foodplaza.domain.exception.ResourceNotFoundException;
import com.example.foodplaza.domain.model.DishModel;
import com.example.foodplaza.domain.spi.persistence.IDishPersistencePort;
import com.example.foodplaza.infrastructure.out.jpa.entity.DishEntity;
import com.example.foodplaza.infrastructure.out.jpa.entity.RestaurantEntity;
import com.example.foodplaza.infrastructure.out.jpa.mapper.IDishEntityMapper;
import com.example.foodplaza.infrastructure.out.jpa.repository.IDishRepository;
import com.example.foodplaza.infrastructure.out.jpa.repository.IRestaurantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@RequiredArgsConstructor
public class DishAdapterImpl implements IDishPersistencePort {

    private final IDishRepository dishRepository;
    private final IDishEntityMapper dishEntityMapper;
    private final IRestaurantRepository restaurantRepository;
    private static final Logger log = LoggerFactory.getLogger(DishAdapterImpl.class);

    @Override
    @Transactional
    public DishModel saveDish(DishModel dishModel) {
        log.info("Saving dish to database: {}", dishModel);

        // Validar que el restaurante existe
        RestaurantEntity restaurant = restaurantRepository.findById(dishModel.getIdRestaurant())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with ID: " + dishModel.getIdRestaurant()));

        DishEntity dishEntity = dishEntityMapper.toDishEntity(dishModel);
        dishEntity.setIdRestaurant(restaurant);

        DishEntity savedDishEntity = dishRepository.save(dishEntity);
        log.info("Dish saved with ID: {}", savedDishEntity.getIdDish());

        return dishEntityMapper.toDishModel(savedDishEntity);
    }

    @Override
    public DishModel getDishById(Long dishId) {
        DishEntity dishEntity = dishRepository.findById(dishId)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found with ID: " + dishId));
        DishModel dishModel = dishEntityMapper.toDishModel(dishEntity);
        dishModel.setIdDish(dishEntity.getIdDish());
        return dishModel;
    }

    @Override
    @Transactional
    public void updateDish(DishModel dishModel) {
        log.info("Updating dish in database with ID: {}", dishModel.getIdDish());

        DishEntity existingDishEntity = dishRepository.findById(dishModel.getIdDish())
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found with ID: " + dishModel.getIdDish()));

        // Actualizar
        existingDishEntity.setPrice(dishModel.getPrice());
        existingDishEntity.setDescription(dishModel.getDescription());
        existingDishEntity.setActive(dishModel.getActive());

        dishRepository.save(existingDishEntity);
        log.info("Dish successfully updated in database.");
    }

    @Override
    @Transactional
    public void deleteDish(Long dishId) {
        dishRepository.deleteById(dishId);
    }

    @Override
    public Page<DishModel> listDishes(Pageable pageable, Long idCategory, Long idRestaurant) {
        if (idCategory != null) {
            return dishRepository.findByIdRestaurantAndIdCategory(idRestaurant, idCategory, pageable)
                    .map(dishEntityMapper::toDishModel);
        } else {
            return dishRepository.findByIdRestaurant(idRestaurant, pageable)
                    .map(dishEntityMapper::toDishModel);
        }
    }

    @Override
    public Page<DishModel> findByiIdRestaurantAndIdCategory(Long idRestaurant, Long idCategory, Pageable pageable) {
        Page<DishEntity> dishEntities = dishRepository.findByIdRestaurantAndIdCategory(idRestaurant, idCategory, pageable);
        return dishEntities.map(dishEntityMapper::toDishModel);
    }

    @Override
    public Page<DishModel> findByIdRestaurant(Long idRestaurant, Pageable pageable) {
        Page<DishEntity> dishEntities = dishRepository.findByIdRestaurant(idRestaurant, pageable);
        return dishEntities.map(dishEntityMapper::toDishModel);
    }
}
