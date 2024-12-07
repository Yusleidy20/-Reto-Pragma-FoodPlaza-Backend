package com.example.foodplaza.domain.usecase;


import com.example.foodplaza.domain.api.IDishServicePort;
import com.example.foodplaza.domain.exception.ResourceNotFoundException;
import com.example.foodplaza.domain.exception.UnauthorizedException;
import com.example.foodplaza.domain.model.DishModel;
import com.example.foodplaza.domain.model.RestaurantModel;
import com.example.foodplaza.domain.spi.persistence.IDishPersistencePort;
import com.example.foodplaza.domain.spi.persistence.IRestaurantPersistencePort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RequiredArgsConstructor
public class DishUseCase implements IDishServicePort {
    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort; // Puerto para Restaurant
    private static final Logger log = LoggerFactory.getLogger(DishUseCase.class);

    // Método para verificar que el usuario es el propietario del restaurante
    void validateOwner(Long idRestaurant, Long userId) {
        RestaurantModel restaurant = restaurantPersistencePort.findById(idRestaurant)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with ID: " + idRestaurant));

        if (!restaurant.getOwnerId().equals(userId)) {
            log.error("Unauthorized access: User with ID {} is not the owner of restaurant ID {}", userId, idRestaurant);
            throw new UnauthorizedException("You are not authorized to perform this action on this restaurant.");
        }
    }

    // Método para guardar los platos
    @Override
    public void saveDish(DishModel dishModel) {
        log.info("Data received in DishModel: {}", dishModel);

        // Obtener el userId desde la seguridad
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = (Long) authentication.getDetails();

        // Validar que el usuario sea el propietario del restaurante
        validateOwner(dishModel.getIdRestaurant(), currentUserId);

        // Guardar el plato
        dishModel.setActive(true);
        dishPersistencePort.saveDish(dishModel);  // Esto interactúa con el Adapter
        log.info("Dish created successfully: {}", dishModel);
    }

    @Override
    @Transactional
    public void updateDish(DishModel dishModel) {
        Long currentUserId = getCurrentUserId();
        DishModel existingDish = dishPersistencePort.getDishById(dishModel.getIdDish());
        validateOwner(existingDish.getIdRestaurant(), currentUserId);
        updateDishFields(existingDish, dishModel);
        dishPersistencePort.updateDish(existingDish);
    }

    // Método para obtener el ID del usuario autenticado
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getDetails();
    }

    // Método para actualizar los campos del plato
    private void updateDishFields(DishModel existingDish, DishModel dishModel) {
        if (dishModel.getPrice() != null) {
            existingDish.setPrice(dishModel.getPrice());
        }
        if (dishModel.getDescription() != null) {
            existingDish.setDescription(dishModel.getDescription());
        }
        if (dishModel.getActive() != null) {
            existingDish.setActive(dishModel.getActive());
        }
    }


    @Override
    public DishModel getDishById(Long idDish) {
        return dishPersistencePort.getDishById(idDish);
    }

    @Override
    public Page<DishModel> listDish(Pageable pageable, Long idCategory, Long idRestaurant) {
        if (idCategory != null) {
            return dishPersistencePort.findByiIdRestaurantAndIdCategory(idRestaurant, idCategory, pageable);
        } else {
            return dishPersistencePort.findByIdRestaurant(idRestaurant, pageable);
        }
    }

}
