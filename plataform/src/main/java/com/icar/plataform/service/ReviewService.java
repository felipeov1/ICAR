package com.icar.plataform.service;

import com.icar.plataform.dto.request.ReviewRequest;
import com.icar.plataform.dto.response.ReviewResponse;
import org.springframework.stereotype.Service;

@Service
public interface ReviewService {
    ReviewResponse create(ReviewRequest dto);
}