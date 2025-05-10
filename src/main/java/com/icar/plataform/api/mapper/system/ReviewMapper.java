package com.icar.plataform.api.mapper.system;

import com.icar.plataform.domain.model.system.Review;
import com.icar.plataform.api.dto.request.system.ReviewRequest;
import com.icar.plataform.api.dto.response.system.ReviewResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")  // This is crucial for Spring DI
public interface ReviewMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Review toEntity(ReviewRequest dto);

    ReviewResponse toDto(Review entity);
}