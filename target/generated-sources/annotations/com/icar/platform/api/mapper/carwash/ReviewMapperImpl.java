package com.icar.platform.api.mapper.carwash;

import com.icar.platform.api.dto.response.carwash.profile.ReviewResponse;
import com.icar.platform.domain.model.carwash.profile.Review;
import com.icar.platform.domain.model.customer.Customer;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-27T22:47:25+0000",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class ReviewMapperImpl implements ReviewMapper {

    @Override
    public ReviewResponse toResponse(Review review) {
        if ( review == null ) {
            return null;
        }

        String customerName = null;
        int rating = 0;
        String comment = null;
        LocalDateTime createdAt = null;

        customerName = reviewCustomerFullName( review );
        if ( review.getRating() != null ) {
            rating = review.getRating();
        }
        comment = review.getComment();
        createdAt = review.getCreatedAt();

        ReviewResponse reviewResponse = new ReviewResponse( customerName, rating, comment, createdAt );

        return reviewResponse;
    }

    private String reviewCustomerFullName(Review review) {
        Customer customer = review.getCustomer();
        if ( customer == null ) {
            return null;
        }
        return customer.getFullName();
    }
}
