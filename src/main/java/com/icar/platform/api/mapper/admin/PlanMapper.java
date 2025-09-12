package com.icar.platform.api.mapper.admin;

import com.icar.platform.api.dto.request.admin.PlanRequest;
import com.icar.platform.api.dto.response.admin.PlanResponse;
import com.icar.platform.domain.model.admin.Plan;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PlanMapper {
    Plan toEntity(PlanRequest request);
    PlanResponse toResponse(Plan plan);
    void updateEntityFromRequest(PlanRequest request, @MappingTarget Plan plan);
}