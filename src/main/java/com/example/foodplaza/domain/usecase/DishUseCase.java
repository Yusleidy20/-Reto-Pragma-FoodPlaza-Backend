package com.example.foodplaza.domain.usecase;

import com.example.foodplaza.application.dto.request.DishUpdateRequestDto;
import com.example.foodplaza.domain.api.IDishServicePort;
import com.example.foodplaza.domain.exception.ResourceNotFoundException;
import com.example.foodplaza.domain.exception.UnauthorizedException;
import com.example.foodplaza.domain.model.DishModel;
import com.example.foodplaza.domain.spi.persistence.IDishPersistencePort;
import com.example.foodplaza.infrastructure.out.jpa.entity.RestaurantEntity;
import com.example.foodplaza.infrastructure.out.jpa.repository.IRestaurantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@RequiredArgsConstructor
public class DishUseCase implements IDishServicePort {
    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantRepository restaurantRepository;
    private static final Logger log = LoggerFactory.getLogger(DishUseCase.class);

    // Método para verificar que el usuario es el propietario del restaurante
    private void validateOwner(Long restaurantId, Long userId) {
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with ID: " + restaurantId));

        if (!restaurant.getOwnerId().equals(userId)) {
            log.error("Unauthorized access: User with ID {} is not the owner of restaurant ID {}", userId, restaurantId);
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
        dishPersistencePort.saveDish(dishModel);
        log.info("Dish created successfully: {}", dishModel);
    }

    // Método para actualizar el plato
    @Override
    @Transactional
    public void updateDish(DishModel dishModel) {
        log.info("Updating dish with ID: {}", dishModel.getIdDish());

        // Obtener el userId desde la seguridad (Spring Security)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = (Long) authentication.getDetails();

        // Obtener el plato actual
        DishModel existingDish = dishPersistencePort.getDishById(dishModel.getIdDish());

        // Validar que el usuario sea el propietario del restaurante
        validateOwner(existingDish.getIdRestaurant(), currentUserId);

        // Actualizar los campos del plato solo si no son nulos
        if (dishModel.getPrice() != null) {
            existingDish.setPrice(dishModel.getPrice());
        }

        if (dishModel.getDescription() != null) {
            existingDish.setDescription(dishModel.getDescription());
        }

        if (dishModel.getActive() != null) {
            existingDish.setActive(dishModel.getActive());  // Aquí se realiza la actualización solo si se pasa el valor
        }

        // Guardar el plato actualizado
        dishPersistencePort.updateDish(existingDish);
        log.info("Dish updated successfully: {}", existingDish);
    }




    @Override
    public DishModel getDishById(Long idDish) {
        return dishPersistencePort.getDishById(idDish);
    }
}
