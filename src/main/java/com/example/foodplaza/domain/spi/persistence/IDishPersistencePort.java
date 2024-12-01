package com.example.foodplaza.domain.spi.persistence;

import com.example.foodplaza.domain.model.DishModel;

public interface IDishPersistencePort {
    DishModel saveDish(DishModel dishModel); // Guardar un plato
    DishModel getDishById(Long dishId); // Encontrar un plato por ID
    // Buscar platos por restaurante
    void updateDish(DishModel dishModel); // Actualizar un plato
    void deleteDish(Long dishId); // Eliminar un plato
}
