package com.icar.platform.api.controller.v1.carwash.profile;

import com.icar.platform.api.dto.request.carwash.profile.ReviewRequest;
import com.icar.platform.api.dto.response.carwash.profile.ReviewResponse;
import com.icar.platform.application.service.carwash.profile.ReviewService;
import com.icar.platform.infrastructure.security.service.UserDetailsImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@Tag(name = "Reviews", description = "Endpoints for managing reviews")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/reviews")
    public ResponseEntity<Void> createReview(
            @Valid @RequestBody ReviewRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        reviewService.createReview(userDetails.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/carwashes/{carWashId}/reviews")
    public ResponseEntity<List<ReviewResponse>> getReviewsForCarWash(@PathVariable UUID carWashId) {
        return ResponseEntity.ok(reviewService.getReviewsForCarWash(carWashId));
    }
}