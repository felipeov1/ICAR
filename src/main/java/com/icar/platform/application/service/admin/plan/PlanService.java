package com.icar.platform.application.service.admin.plan;

import com.icar.platform.api.dto.request.admin.PlanRequest;
import com.icar.platform.api.dto.response.admin.PlanResponse;
import com.icar.platform.api.mapper.admin.PlanMapper;
import com.icar.platform.domain.model.admin.Plan;
import com.icar.platform.domain.repository.admin.PlanRepository;
import com.icar.platform.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final PlanMapper planMapper;

    @Transactional(readOnly = true)
    public List<PlanResponse> findAllActivePlans() {
        return planRepository.findAll().stream()
                .map(planMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PlanResponse createPlan(PlanRequest request) {
        Plan plan = planMapper.toEntity(request);
        Plan savedPlan = planRepository.save(plan);
        return planMapper.toResponse(savedPlan);
    }

    @Transactional
    public PlanResponse updatePlan(UUID id, PlanRequest request) {
        Plan existingPlan = planRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plano não encontrado com ID: " + id));

        planMapper.updateEntityFromRequest(request, existingPlan);
        Plan updatedPlan = planRepository.save(existingPlan);
        return planMapper.toResponse(updatedPlan);
    }

    @Transactional
    public void deactivatePlan(UUID id) {
        if (!planRepository.existsById(id)) {
            throw new ResourceNotFoundException("Plano não encontrado com ID: " + id);
        }
        planRepository.deleteById(id);
    }
}