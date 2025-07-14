package com.icar.platform.api.mapper.carwash;

import com.icar.platform.api.dto.response.carwash.profile.ReviewResponse;
import com.icar.platform.domain.model.carwash.profile.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "customerName", source = "customer.fullName")
    ReviewResponse toResponse(Review review);
}
