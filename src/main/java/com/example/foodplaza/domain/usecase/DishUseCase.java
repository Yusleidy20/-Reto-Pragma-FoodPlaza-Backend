package com.example.foodplaza.domain.usecase;

import com.example.foodplaza.domain.api.IDishServicePort;
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
        log.info("Servicio: Guardando plato: {}", dishModel);
        dishModel.setActive(true);
        dishPersistencePort.saveDish(dishModel);
        log.info("Servicio: Plato guardado correctamente.");
    }

    @Override
    public DishModel getDishById(Long idDish) {
        return dishPersistencePort.findDishById(idDish);
    }
}
