package com.example.foodplaza.domain.api;

import com.example.foodplaza.domain.model.DishModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface IDishServicePort {
    void saveDish(DishModel dishModel) throws IllegalAccessException;
    void updateDish(DishModel dishModel);
    DishModel getDishById(Long idDish);
    // Método para listar los platos paginados y filtrados por restaurante y categoría
    Page<DishModel> listDish(Pageable pageable, Long idCategory, Long idRestaurant);
}
