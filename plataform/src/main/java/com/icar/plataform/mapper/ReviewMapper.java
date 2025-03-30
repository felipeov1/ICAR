package com.icar.plataform.mapper;

import com.icar.plataform.domain.model.Review;
import com.icar.plataform.dto.request.ReviewRequest;
import com.icar.plataform.dto.response.ReviewResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")  // This is crucial for Spring DI
public interface ReviewMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Review toEntity(ReviewRequest dto);

    ReviewResponse toDto(Review entity);
}