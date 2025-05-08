package com.icar.plataform.api.mapper;

import com.icar.plataform.domain.model.CarWash;
import com.icar.plataform.api.dto.request.CarWashRequest;
import com.icar.plataform.api.dto.response.CarWashResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CarWashMapper {

    @Mapping(target = "id", ignore = true)
    CarWash toEntity(CarWashRequest dto);

    @Mapping(target = "averageRating", expression = "java(entity.getAverageRating() != null ? entity.getAverageRating() : 0.0)")
    CarWashResponse toDto(CarWash entity);
}