package com.icar.platform.application.service.carwash.legal;

import com.icar.platform.api.dto.request.carwash.CarWashRegistrationRequest;
import com.icar.platform.api.dto.response.carwash.CarWashRegistrationResponse;
import com.icar.platform.api.mapper.carwash.CarWashRegistrationMapper;
import com.icar.platform.domain.model.carwash.legal.CarWashRegistration;
import com.icar.platform.domain.repository.carwash.legal.CarWashRegistrationDataRepository;
import com.icar.platform.infrastructure.validation.exception.CustomValidationException;
import com.icar.platform.infrastructure.validation.exception.ValidationError;
import com.icar.platform.shared.exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarWashRegistrationServiceImpl implements CarWashRegistrationService {

    private final CarWashRegistrationDataRepository repository;
    private final CarWashRegistrationMapper mapper;
    private final EntityManager entityManager;
    private final PasswordEncoder passwordEncoder;

    private void enableNotDeletedFilter() {
        entityManager.unwrap(org.hibernate.Session.class)
                .enableFilter("notDeleted")
                .setParameter("isNull", true);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarWashRegistrationResponse> findAllByOrderByCreatedAtAsc() {
        enableNotDeletedFilter();
        return repository.findAllByDeletedAtIsNullOrderByCreatedAtAsc().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CarWashRegistrationResponse findById(UUID id) {
        enableNotDeletedFilter();
        return repository.findByIdAndDeletedAtIsNull(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Lavagem de carro não encontrada ou inativa"));
    }

    @Override
    @Transactional
    public CarWashRegistrationResponse update(UUID id, CarWashRegistrationRequest request) {
        CarWashRegistration existing = repository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lavagem de carro não encontrada ou inativa"));

        validateRequest(request);

        existing.setCnpj(request.cnpj());
        existing.setCpf(request.cpf());
        existing.setLegalName(request.legalName());
        existing.setTradeName(request.tradeName());
        existing.setOwnerName(request.ownerName());
        existing.setPhone(request.phone());
        existing.setEmail(request.email());

        existing = repository.save(existing);
        return mapper.toDto(existing);
    }

    @Override
    @Transactional
    public void deactivate(UUID id) {
        CarWashRegistration existing = repository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lavagem de carro não encontrada ou já está inativa"));
        existing.setDeletedAt(LocalDateTime.now());
        repository.save(existing);
    }

    @Override
    @Transactional
    public CarWashRegistrationResponse create(CarWashRegistrationRequest request) {
        validateRequest(request);

        CarWashRegistration entity = mapper.toEntity(request);
        entity.setPassword(passwordEncoder.encode(request.password()));
        entity.setDeletedAt(null);

        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    private void validateRequest(CarWashRegistrationRequest request) {
        List<ValidationError> errors = new ArrayList<>();

        if (request.cnpj() != null && !request.cnpj().isBlank() && !isValidCnpj(request.cnpj())) {
            errors.add(ValidationError.builder()
                    .field("cnpj")
                    .message("Formato de CNPJ inválido")
                    .errorCode("invalid_cnpj")
                    .build());
        }

        if (request.email() == null || request.email().isBlank() || !isValidEmail(request.email())) {
            errors.add(ValidationError.builder()
                    .field("email")
                    .message("Formato de email inválido")
                    .errorCode("invalid_email")
                    .build());
        }

        if (request.password() == null || request.password().isBlank()) {
            errors.add(ValidationError.builder()
                    .field("senha")
                    .message("A senha é obrigatória")
                    .errorCode("required")
                    .build());
        }

        if (!errors.isEmpty()) {
            throw new CustomValidationException(errors);
        }
    }

    private boolean isValidCnpj(String cnpj) {
        return cnpj.matches("\\d{14}");
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[\\w-.]+@[\\w-]+\\.[a-zA-Z]{2,}$");
    }
}