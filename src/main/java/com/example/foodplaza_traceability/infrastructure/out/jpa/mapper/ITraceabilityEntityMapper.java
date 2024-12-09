package com.example.foodplaza_traceability.infrastructure.out.jpa.mapper;

import com.example.foodplaza_traceability.domain.model.TraceabilityModel;
import com.example.foodplaza_traceability.infrastructure.out.jpa.entity.TraceabilityEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ITraceabilityEntityMapper {
    TraceabilityEntity toEntity(TraceabilityModel model);

    TraceabilityModel toModel(TraceabilityEntity entity);
}
