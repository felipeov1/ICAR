package com.icar.plataform.api.mapper.system;

import com.icar.plataform.api.dto.request.system.ReviewRequest;
import com.icar.plataform.api.dto.response.system.ReviewResponse;
import com.icar.plataform.domain.model.system.Review;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-12T19:41:32-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Microsoft)"
)
@Component
public class ReviewMapperImpl implements ReviewMapper {

    @Override
    public Review toEntity(ReviewRequest dto) {
        if ( dto == null ) {
            return null;
        }

        Review review = new Review();

        review.setCustomerId( dto.customerId() );
        review.setCarWashId( dto.carWashId() );
        review.setServiceId( dto.serviceId() );
        review.setRating( dto.rating() );
        review.setFeedback( dto.feedback() );

        return review;
    }

    @Override
    public ReviewResponse toDto(Review entity) {
        if ( entity == null ) {
            return null;
        }

        UUID id = null;
        UUID customerId = null;
        UUID carWashId = null;
        UUID serviceId = null;
        Integer rating = null;
        String feedback = null;
        LocalDateTime createdAt = null;

        id = entity.getId();
        customerId = entity.getCustomerId();
        carWashId = entity.getCarWashId();
        serviceId = entity.getServiceId();
        rating = entity.getRating();
        feedback = entity.getFeedback();
        createdAt = entity.getCreatedAt();

        ReviewResponse reviewResponse = new ReviewResponse( id, customerId, carWashId, serviceId, rating, feedback, createdAt );

        return reviewResponse;
    }
}
