package com.example.foodplaza.infrastructure.out.jpa.adapter;

import com.example.foodplaza.domain.model.DishModel;
import com.example.foodplaza.domain.model.OrderDishModel;
import com.example.foodplaza.domain.model.OrderModel;
import com.example.foodplaza.domain.model.RestaurantModel;
import com.example.foodplaza.domain.spi.persistence.IOrderPersistencePort;
import com.example.foodplaza.infrastructure.configuration.Constants;
import com.example.foodplaza.infrastructure.out.jpa.entity.DishEntity;
import com.example.foodplaza.infrastructure.out.jpa.entity.OrderDishEntity;
import com.example.foodplaza.infrastructure.out.jpa.entity.OrderEntity;
import com.example.foodplaza.infrastructure.out.jpa.entity.RestaurantEntity;
import com.example.foodplaza.infrastructure.out.jpa.feignclients.mapper.ITraceabilityFeignClient;
import com.example.foodplaza.infrastructure.out.jpa.mapper.IOrderEntityMapper;
import com.example.foodplaza.infrastructure.out.jpa.repository.IDishRepository;
import com.example.foodplaza.infrastructure.out.jpa.repository.IOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class OrderAdapterImpl implements IOrderPersistencePort {
    private final IOrderRepository orderRepository;
    private final IDishRepository dishRepository;
    private final IOrderEntityMapper orderEntityMapper;

    @Override
    public OrderModel saveOrder(OrderModel orderModel) {

        // Validar que el PIN esté presente si el estado es READY
        if (Constants.DELIVERED.equals(orderModel.getStateOrder()) && (orderModel.getSecurityPin() == null || orderModel.getSecurityPin().isEmpty())) {
            throw new IllegalArgumentException("Security PIN is required for orders in READY state.");
        }

        // Usar mapToEntity para convertir manualmente
        OrderEntity orderEntity = mapToEntity(orderModel);

        // Configurar relaciones para los platos
        List<OrderDishEntity> dishEntities = orderModel.getOrderDishes().stream()
                .map(this::mapOrderDishToEntity)
                .toList();
        dishEntities.forEach(dish -> dish.setOrders(orderEntity));
        orderEntity.setOrderDishes(dishEntities);

        // Guardar en la base de datos
        OrderEntity savedEntity = orderRepository.save(orderEntity);

        // Convertir de vuelta a modelo para la respuesta
        return mapToModelWithDishes(savedEntity);
    }





    @Override
    public Optional<OrderModel> getOrderById(Long idOrder) {
        // Llama a la consulta personalizada que incluye los `OrderDishes`
        return orderRepository.findByIdWithDishes(idOrder)
                .map(this::mapToModelWithDishes);
    }



    @Override
    public List<OrderModel> getOrdersByChefId(Long chefId) {
        return orderRepository.findByChefId(chefId).stream()
                .map(orderEntityMapper::toModel)
                .toList();
    }

    @Override
    public boolean validateDishesBelongToRestaurant(Long restaurantId, List<Long> dishIds) {
        if (dishIds == null || dishIds.isEmpty()) {
            throw new IllegalArgumentException("The dish list cannot be empty.");
        }

        List<DishEntity> dishes = dishRepository.findAllById(dishIds);

        if (dishes.isEmpty()) {
            throw new IllegalArgumentException("No dishes found for the provided IDs.");
        }

        return dishes.stream().allMatch(dish ->
                dish.getIdRestaurant() != null &&
                        dish.getIdRestaurant().getIdRestaurant().equals(restaurantId)
        );
    }

    @Override
    public boolean hasActiveOrders(Long chefId) {
        List<OrderEntity> activeOrders = orderRepository.findByChefIdAndStateOrderIn(
                chefId, List.of(Constants.PENDING, Constants.IN_PREPARATION, Constants.READY)
        );
        return !activeOrders.isEmpty();
    }

    @Override
    public Page<OrderModel> getOrdersByStateAndRestaurant(String stateOrder, Long idRestaurant, Pageable pageable) {
        return orderRepository.findByStateOrderAndRestaurant_IdRestaurantWithDishes(stateOrder, idRestaurant, pageable)
                .map(this::mapToModel);
    }



    private OrderEntity mapToEntity(OrderModel model) {
        OrderEntity entity = new OrderEntity();

        entity.setIdOrder(model.getIdOrder());
        entity.setChefId(model.getChefId());
        entity.setDateOrder(model.getDateOrder() != null ? model.getDateOrder() : LocalDate.now());
        entity.setStateOrder(model.getStateOrder());
        entity.setCustomerId(model.getCustomerId());
        entity.setSecurityPin(model.getSecurityPin()); // Asignar el PIN de seguridad

        if (model.getRestaurant() != null) {
            RestaurantEntity restaurantEntity = new RestaurantEntity();
            restaurantEntity.setIdRestaurant(model.getRestaurant().getIdRestaurant());
            entity.setRestaurant(restaurantEntity);
        }

        return entity;
    }



    private OrderModel mapToModel(OrderEntity entity) {
        OrderModel model = new OrderModel();

        model.setIdOrder(entity.getIdOrder());
        model.setChefId(entity.getChefId());
        model.setCustomerId(entity.getCustomerId()); // Este valor debe estar presente
        model.setDateOrder(entity.getDateOrder());
        model.setStateOrder(entity.getStateOrder());

        if (entity.getRestaurant() != null) {
            RestaurantModel restaurantModel = new RestaurantModel();
            restaurantModel.setIdRestaurant(entity.getRestaurant().getIdRestaurant());
            model.setRestaurant(restaurantModel);
        }

        if (entity.getOrderDishes() != null && !entity.getOrderDishes().isEmpty()) {
            List<OrderDishModel> dishModels = entity.getOrderDishes().stream()
                    .map(this::mapOrderDishToModel)
                    .toList();
            model.setOrderDishes(dishModels);
        }

        return model;
    }


    private OrderDishModel mapOrderDishToModel(OrderDishEntity entity) {
        OrderDishModel model = new OrderDishModel();

        model.setIdOrderDish(entity.getIdOrderDish());
        model.setAmount(entity.getAmount());

        if (entity.getDish() != null) {
            DishModel dishModel = new DishModel();
            dishModel.setIdDish(entity.getDish().getIdDish());
            dishModel.setNameDish(entity.getDish().getNameDish());
            dishModel.setPrice(entity.getDish().getPrice());
            dishModel.setDescription(entity.getDish().getDescription());
            dishModel.setUrlImage(entity.getDish().getUrlImage());
            model.setDish(dishModel);
        }

        return model;
    }







    private OrderDishEntity mapOrderDishToEntity(OrderDishModel model) {
        // Crear una nueva instancia de OrderDishEntity
        OrderDishEntity entity = new OrderDishEntity();

        // Asignar la cantidad
        entity.setAmount(model.getAmount());

        // Validar y asignar el plato si está presente
        if (model.getDish() != null) {
            // Cargar el DishEntity completo desde la base de datos utilizando su ID
            DishEntity dishEntity = dishRepository.findById(model.getDish().getIdDish())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Dish not found for ID: " + model.getDish().getIdDish())); // Excepción si no se encuentra
            entity.setDish(dishEntity);
        }

        // Validar y asignar el pedido si está presente
        if (model.getIdOrder() != null) {
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setIdOrder(model.getIdOrder());
            entity.setOrders(orderEntity); // Relación con el pedido
        }

        return entity; // Retornar la entidad completa
    }


    private OrderModel mapToModelWithDishes(OrderEntity entity) {
        OrderModel model = new OrderModel();

        model.setIdOrder(entity.getIdOrder());
        model.setChefId(entity.getChefId());
        model.setCustomerId(entity.getCustomerId()); // Asegúrate de mapear customerId
        model.setDateOrder(entity.getDateOrder());
        model.setStateOrder(entity.getStateOrder());
        model.setSecurityPin(entity.getSecurityPin()); // Asignar el PIN aquí

        if (entity.getRestaurant() != null) {
            RestaurantModel restaurantModel = new RestaurantModel();
            restaurantModel.setIdRestaurant(entity.getRestaurant().getIdRestaurant());
            model.setRestaurant(restaurantModel);
        }

        if (entity.getOrderDishes() != null) {
            List<OrderDishModel> dishModels = entity.getOrderDishes().stream()
                    .map(this::mapOrderDishToModel)
                    .toList();
            model.setOrderDishes(dishModels);
        }

        return model;
    }




}