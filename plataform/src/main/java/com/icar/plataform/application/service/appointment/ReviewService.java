package com.icar.plataform.application.service.appointment;

import com.icar.plataform.api.dto.request.ReviewRequest;
import com.icar.plataform.api.dto.response.ReviewResponse;
import org.springframework.stereotype.Service;

@Service
public interface ReviewService {
    ReviewResponse create(ReviewRequest dto);
}