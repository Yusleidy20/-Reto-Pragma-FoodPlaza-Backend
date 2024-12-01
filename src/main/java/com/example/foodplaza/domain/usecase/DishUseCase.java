package com.example.foodplaza.domain.usecase;

import com.example.foodplaza.domain.api.IDishServicePort;
import com.example.foodplaza.domain.exception.ResourceNotFoundException;
import com.example.foodplaza.domain.model.DishModel;
import com.example.foodplaza.domain.spi.persistence.IDishPersistencePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DishUseCase implements IDishServicePort {
    private final IDishPersistencePort dishPersistencePort;
    private static final Logger log = LoggerFactory.getLogger(DishUseCase.class);

    public DishUseCase(IDishPersistencePort dishPersistencePort) {

        this.dishPersistencePort = dishPersistencePort;
    }

    @Override
    public void saveDish(DishModel dishModel) {
        dishModel.setActive(true);
        dishPersistencePort.saveDish(dishModel);
    }
    @Override
    public void updateDish(DishModel dishModel) {
        // Buscar el plato actual
        DishModel existingDish = dishPersistencePort.getDishById(dishModel.getIdDish());
        if (existingDish == null) {
            throw new ResourceNotFoundException("Dish not found with ID: " + dishModel.getIdDish());
        }

        existingDish.setPrice(dishModel.getPrice());
        existingDish.setDescription(dishModel.getDescription());

        dishPersistencePort.updateDish(existingDish);
        log.info("Dish update correctly: {}", existingDish);
    }
    @Override
    public DishModel getDishById(Long idDish) {
        return dishPersistencePort.getDishById(idDish);
    }
}
