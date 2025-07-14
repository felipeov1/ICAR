package com.icar.platform.application.service.carwash.profile;

import com.icar.platform.api.dto.request.carwash.profile.ReviewRequest;
import com.icar.platform.api.dto.response.carwash.profile.ReviewResponse;
import com.icar.platform.api.mapper.carwash.ReviewMapper;
import com.icar.platform.domain.enums.AppointmentStatus;
import com.icar.platform.domain.model.appointment.CarWashAppointment;
import com.icar.platform.domain.model.carwash.profile.CarWashProfile;
import com.icar.platform.domain.model.carwash.profile.Review;
import com.icar.platform.domain.model.customer.Customer;
import com.icar.platform.domain.repository.appointment.CarWashAppointmentRepository;
import com.icar.platform.domain.repository.carwash.profile.CarWashProfileRepository;
import com.icar.platform.domain.repository.carwash.profile.ReviewRepository;
import com.icar.platform.domain.repository.customer.CustomerRepository;
import com.icar.platform.shared.exception.BusinessException;
import com.icar.platform.shared.exception.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final CarWashAppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final CarWashProfileRepository profileRepository;
    private final ReviewMapper reviewMapper;

    @Override
    @Transactional
    public void createReview(UUID customerId, ReviewRequest request) {

        if (reviewRepository.existsByAppointmentId(request.appointmentId())) {
            throw new BusinessException("Este agendamento já foi avaliado.");
        }
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));
        CarWashAppointment appointment = appointmentRepository.findByIdAndCustomerId(request.appointmentId(), customerId).orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado ou não pertence ao cliente."));

        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            throw new BusinessException("Só é possível avaliar agendamentos finalizados.");
        }

        Review review = new Review();
        review.setCustomer(customer);
        review.setAppointment(appointment);
        review.setProfile(appointment.getProfile());
        review.setRating(request.rating());
        review.setComment(request.comment());
        reviewRepository.save(review);

        updateCarWashProfileRating(appointment.getProfile().getId());
    }

    @Transactional
    public void updateCarWashProfileRating(UUID profileId) {
        CarWashProfile profile = profileRepository.findById(profileId).orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado."));

        Double average = reviewRepository.calculateAverageRating(profileId);
        long count = reviewRepository.countByProfileId(profileId);

        profile.setRating(BigDecimal.valueOf(average).setScale(2, RoundingMode.HALF_UP));
        profile.setReviews((int) count);

        profileRepository.save(profile);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsForCarWash(UUID carWashId) {
        CarWashProfile profile = profileRepository.findById(carWashId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil do Lava-rápido não encontrado."));

        return reviewRepository.findByProfileIdOrderByCreatedAtDesc(profile.getId())
                .stream()
                .map(reviewMapper::toResponse)
                .collect(Collectors.toList());
    }
}
