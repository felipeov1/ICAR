package com.icar.platform.api.mapper.carwash;

import com.icar.platform.api.dto.request.carwash.profile.CompanyCustomerRequest;
import com.icar.platform.api.dto.response.carwash.profile.CompanyCustomerResponse;
import com.icar.platform.domain.model.carwash.profile.CompanyCustomer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CompanyCustomerMapper {

    @Mapping(target = "name", source = "fullName")
    @Mapping(target = "number", source = "streetNumber")
    CompanyCustomerResponse toResponse(CompanyCustomer entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "streetNumber", source = "streetNumber")
    CompanyCustomer toEntity(CompanyCustomerRequest request);
}
