package com.example.foodplaza.domain.spi.persistence;

import com.example.foodplaza.domain.model.DishModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IDishPersistencePort {
    DishModel saveDish(DishModel dishModel);
    DishModel getDishById(Long dishId);

    void updateDish(DishModel dishModel);
    void deleteDish(Long dishId);
    Page<DishModel> listDishes(Pageable pageable, Long idCategory, Long idRestaurant);
    Page<DishModel> findByiIdRestaurantAndIdCategory(Long idRestaurant, Long idCategory, Pageable pageable);
    Page<DishModel> findByIdRestaurant(Long idRestaurant, Pageable pageable);
}
