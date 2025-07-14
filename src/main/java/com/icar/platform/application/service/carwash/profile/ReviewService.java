package com.icar.platform.application.service.carwash.profile;

import com.icar.platform.api.dto.request.carwash.profile.ReviewRequest;
import com.icar.platform.api.dto.response.carwash.profile.ReviewResponse;

import java.util.List;
import java.util.UUID;

public interface ReviewService {
    void createReview(UUID customerId, ReviewRequest request);
    List<ReviewResponse> getReviewsForCarWash(UUID carWashId);
}
