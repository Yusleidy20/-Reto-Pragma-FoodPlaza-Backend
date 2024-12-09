package com.example.foodplaza.infrastructure.out.jpa.adapter;

import com.example.foodplaza.application.dto.response.RestaurantDto;
import com.example.foodplaza.domain.model.RestaurantModel;
import com.example.foodplaza.domain.spi.persistence.IRestaurantPersistencePort;
import com.example.foodplaza.infrastructure.out.jpa.entity.RestaurantEntity;
import com.example.foodplaza.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.example.foodplaza.infrastructure.out.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RestaurantAdapterImpl implements IRestaurantPersistencePort {
    private final IRestaurantRepository restaurantRepositoryMySql;
    private  final IRestaurantEntityMapper restaurantEntityMapper;
    private static final Logger log = LoggerFactory.getLogger(RestaurantAdapterImpl.class);

    @Override
    public RestaurantModel saveRestaurant(RestaurantModel restaurantModel) {
        RestaurantEntity restaurantEntity = restaurantRepositoryMySql.save(restaurantEntityMapper.toRestaurantEntity(restaurantModel));
        log.info("Entity mapped before saving: {}", restaurantEntity);
        return restaurantEntityMapper.toRestaurantModel(restaurantEntity);
    }

    @Override
    public Page<RestaurantDto> getRestaurantsWithPaginationAndSorting(org.springframework.data.domain.Pageable pageable) {
        // Obtener las entidades desde el repositorio
        Page<RestaurantEntity> restaurantEntities = restaurantRepositoryMySql.findAll(pageable);

        // Mapear las entidades a DTOs con solo los campos necesarios
        return restaurantEntities.map(restaurantEntity -> {
            RestaurantDto dto = new RestaurantDto();
            dto.setNameRestaurant(restaurantEntity.getNameRestaurant());
            dto.setUrlLogo(restaurantEntity.getUrlLogo());
            return dto;
        });
    }



    @Override
    public RestaurantModel getRestaurantById(Long idRestaurant) {
        Optional<RestaurantEntity> optionalRestaurantEntity = restaurantRepositoryMySql.findById(idRestaurant);
        log.info("Fetched RestaurantEntity: {}", optionalRestaurantEntity);

        if (optionalRestaurantEntity.isEmpty()) {
            throw new NoSuchElementException("Restaurant with ID " + idRestaurant + " not found.");
        }

        return restaurantEntityMapper.toRestaurantModel(optionalRestaurantEntity.get());
    }




    @Override
    public RestaurantModel getRestaurantByIdOwner(Long ownerId) {
        Optional<RestaurantEntity> optionalRestaurantEntity = restaurantRepositoryMySql.findByOwnerId(ownerId);
        RestaurantEntity restaurantEntity = optionalRestaurantEntity.orElse(null);
        return restaurantEntityMapper.toRestaurantModel(restaurantEntity);
    }

    @Override
    public boolean existsByNit(String nit) {
        return restaurantRepositoryMySql.existsByNit(nit);
    }

    @Override
    public Optional<RestaurantModel> findById(Long idRestaurant) {
        Optional<RestaurantEntity> optionalRestaurantEntity = restaurantRepositoryMySql.findById(idRestaurant);

        // Si la entidad existe, la mapeamos a RestaurantModel, sino devolvemos un Optional vacío
        return optionalRestaurantEntity.map(restaurantEntityMapper::toRestaurantModel);
    }

    @Override
    public List<RestaurantModel> getAllRestaurants() {
        return restaurantRepositoryMySql.findAll().stream()
                .map(this::convertToModel)
                .toList(); // Lista inmutable y más eficiente
    }

    @Override
    public List<Long> getOrderIdsByRestaurantId(Long idRestaurant) {
        return restaurantRepositoryMySql.findOrderIdsByRestaurantId(idRestaurant);
    }

    @Override
    public boolean validateDishesBelongToRestaurant(Long idRestaurant, List<Long> dishIds) {
        // Lógica para validar los platos
        List<Long> dishIdsFromRestaurant = restaurantRepositoryMySql.findDishIdsByRestaurantId(idRestaurant);

        // Verificar si todos los IDs de los platos están en la lista de IDs del restaurante
        return dishIds.stream().allMatch(dishIdsFromRestaurant::contains);
    }


    private RestaurantModel convertToModel(RestaurantEntity entity) {
        return new RestaurantModel(
                entity.getIdRestaurant(),
                entity.getNameRestaurant(),
                entity.getNit(),
                entity.getAddress(),
                entity.getPhoneNumber(),
                entity.getUrlLogo(),
                entity.getOwnerId()
        );
    }

}

