package com.example.foodplaza.domain.usecase;


import com.example.foodplaza.domain.exception.UnauthorizedException;
import com.example.foodplaza.domain.model.CategoryModel;
import com.example.foodplaza.domain.model.DishModel;
import com.example.foodplaza.domain.model.RestaurantModel;
import com.example.foodplaza.domain.spi.persistence.IDishPersistencePort;
import com.example.foodplaza.domain.spi.persistence.IRestaurantPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class DishUseCaseTest {

   @Mock
   private IDishPersistencePort dishPersistencePort;

   @Mock
   private IRestaurantPersistencePort restaurantPersistencePort;

   @InjectMocks
   private DishUseCase dishUseCase;

   @Mock
   private Authentication authentication;

   private static final Long RESTAURANT_ID = 1L;
   private static final Long OWNER_ID = 100L;
   private static final Long DISH_ID = 10L;
   CategoryModel categoryModel = new CategoryModel(1L, "Main Course", "Main course dishes");

   private RestaurantModel restaurantModel;
   private DishModel dishModel;

   @BeforeEach
   void setUp() {
      // Configuración de datos base para pruebas
      restaurantModel = new RestaurantModel(RESTAURANT_ID, "Test Restaurant","554444","Address", "+567425", "http:12239043.com" ,OWNER_ID);
      dishModel = new DishModel(DISH_ID, "Dish Name", 100, "Description", "Local.png", categoryModel, 1L,true);
   }
   @Test
   void saveDish_shouldSaveDish_whenUserIsOwner() {
      // Configuración del mock para el contexto de seguridad
      SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
      SecurityContextHolder.getContext().setAuthentication(authentication);

      when(authentication.getDetails()).thenReturn(OWNER_ID);

      // Configuración del mock para el restaurante
      when(restaurantPersistencePort.findById(RESTAURANT_ID)).thenReturn(Optional.of(restaurantModel));

      // Acción
      dishUseCase.saveDish(dishModel);

      // Verificaciones
      verify(restaurantPersistencePort).findById(RESTAURANT_ID);
      verify(dishPersistencePort).saveDish(dishModel);

      assertTrue(dishModel.getActive(), "Dish should be active by default");
   }
   @Test
   void saveDish_shouldThrowUnauthorizedException_whenUserIsNotOwner() {
      // Configuración del mock para el contexto de seguridad
      SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
      SecurityContextHolder.getContext().setAuthentication(authentication);

      when(authentication.getDetails()).thenReturn(999L); // Usuario no propietario

      // Configuración del mock para el restaurante
      when(restaurantPersistencePort.findById(RESTAURANT_ID)).thenReturn(Optional.of(restaurantModel));

      // Verificaciones de excepción
      UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
         dishUseCase.saveDish(dishModel);
      });

      assertEquals("You are not authorized to perform this action on this restaurant.", exception.getMessage());
   }
   @Test
   void updateDish_shouldUpdateFields_whenUserIsOwner() {
      // Configuración del mock para el contexto de seguridad
      SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
      SecurityContextHolder.getContext().setAuthentication(authentication);

      when(authentication.getDetails()).thenReturn(OWNER_ID);

      // Configuración del mock para el restaurante y plato
      when(restaurantPersistencePort.findById(RESTAURANT_ID)).thenReturn(Optional.of(restaurantModel));
      when(dishPersistencePort.getDishById(DISH_ID)).thenReturn(dishModel);

      DishModel updatedDish = new DishModel(DISH_ID, null, 200, "Updated Description","Local.png", null, RESTAURANT_ID, true);
      dishUseCase.updateDish(updatedDish);

      // Verificaciones
      assertEquals(200, dishModel.getPrice());
      assertEquals("Updated Description", dishModel.getDescription());

      verify(dishPersistencePort).updateDish(dishModel);
   }

}
