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

        // Mapear y asignar la entidad de restaurante
        DishEntity dishEntity = dishEntityMapper.toDishEntity(dishModel);
        dishEntity.setIdRestaurant(restaurant);

        // Guardar el plato
        DishEntity savedDishEntity = dishRepository.save(dishEntity);
        log.info("Dish saved with ID: {}", savedDishEntity.getIdDish());

        return dishEntityMapper.toDishModel(savedDishEntity);
    }


    @Override
    public DishModel getDishById(Long dishId) {
        DishEntity dishEntity = dishRepository.findById(dishId)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found with ID: " + dishId));

        // Mapear correctamente la entidad al modelo
        DishModel dishModel = dishEntityMapper.toDishModel(dishEntity);
        dishModel.setIdDish(dishEntity.getIdDish());
        return dishModel;
    }


    @Override
    @Transactional
    public void updateDish(DishModel dishModel) {
        log.info("Updating dish in database with ID: {}", dishModel.getIdDish());

        // Buscar la entidad existente
        DishEntity existingDishEntity = dishRepository.findById(dishModel.getIdDish())
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found with ID: " + dishModel.getIdDish()));

        // Actualizar los campos permitidos, incluyendo 'active'
        existingDishEntity.setPrice(dishModel.getPrice());
        existingDishEntity.setDescription(dishModel.getDescription());
        existingDishEntity.setActive(dishModel.getActive());  // Aquí actualizas el campo 'active'

        // Guardar los cambios
        dishRepository.save(existingDishEntity);
        log.info("Dish successfully updated in database.");
    }




    @Override
    @Transactional
    public void deleteDish(Long dishId) {
        dishRepository.deleteById(dishId);
    }

}

