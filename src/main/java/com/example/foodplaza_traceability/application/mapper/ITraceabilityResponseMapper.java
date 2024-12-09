package com.example.foodplaza_traceability.application.mapper;

import com.example.foodplaza_traceability.application.dto.TraceabilityResponseDto;
import com.example.foodplaza_traceability.domain.model.TraceabilityModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ITraceabilityResponseMapper {

    @Mapping(target = "date", dateFormat = "yyyy-MM-dd HH:mm:ss") // Formateo opcional de la fecha
    TraceabilityResponseDto toDto(TraceabilityModel traceabilityModel);

    // Para mapear listas completas
    List<TraceabilityResponseDto> toDtoList(List<TraceabilityModel> traceabilityModels);
}
