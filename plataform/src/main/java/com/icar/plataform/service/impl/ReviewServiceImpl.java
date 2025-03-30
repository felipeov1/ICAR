package com.icar.plataform.service.impl;

import com.icar.plataform.domain.model.*;
import com.icar.plataform.dto.request.ReviewRequest;
import com.icar.plataform.dto.response.ReviewResponse;
import com.icar.plataform.mapper.ReviewMapper;
import com.icar.plataform.repository.*;
import com.icar.plataform.service.ReviewService;
import com.icar.plataform.service.validation.ReviewValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final AppointmentRepository appointmentRepository;
    private final CarWashRepository carWashRepository;
    private final ReviewValidator validator;
    private final ReviewMapper reviewMapper;

    @Override
    @Transactional
    public ReviewResponse create(ReviewRequest dto) {
        // Validar se cliente usou o serviço
        validator.validateCustomerEligibility(dto.customerId(), dto.carWashId());

        Review review = reviewMapper.toEntity(dto);
        Review saved = reviewRepository.save(review);

        // Atualizar rating médio do car wash
        updateCarWashRating(dto.carWashId());

        return reviewMapper.toDto(saved);
    }

    private void updateCarWashRating(UUID carWashId) {
        Double newRating = reviewRepository.calculateAverageRatingByCarWashId(carWashId);
        carWashRepository.updateRating(carWashId, newRating);
    }
}