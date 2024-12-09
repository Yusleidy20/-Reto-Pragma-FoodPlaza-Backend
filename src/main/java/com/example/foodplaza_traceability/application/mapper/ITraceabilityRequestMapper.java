package com.example.foodplaza_traceability.application.mapper;

import com.example.foodplaza_traceability.application.dto.TraceabilityRequestDto;
import com.example.foodplaza_traceability.domain.model.TraceabilityModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ITraceabilityRequestMapper {

    @Mapping(target = "idTraceability", ignore = true) // El ID se genera automáticamente
    @Mapping(target = "date", expression = "java(java.time.LocalDateTime.now())") // Fecha se genera automáticamente
    TraceabilityModel toModel(TraceabilityRequestDto traceabilityRequestDto);
}
