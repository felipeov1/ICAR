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
import java.util.Objects;
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

        validateRequest(request, id);

        existing.setCnpj(request.cnpj());
        existing.setCpf(request.cpf());
        existing.setTradeName(request.tradeName());
        existing.setOwnerName(request.ownerName());
        existing.setPhone(request.phone());
        existing.setEmail(request.email());

        existing.setCity(request.city());
        existing.setState(request.state());
        existing.setZipCode(request.zipCode());

        // Opcional: permitir atualização de senha
        if (request.password() != null && !request.password().isBlank()) {
            existing.setPassword(passwordEncoder.encode(request.password()));
        }

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
        validateRequest(request, null);

        if (request.password() == null || request.password().isBlank()) {
            throw new CustomValidationException(List.of(ValidationError.builder()
                    .field("password")
                    .message("A senha é obrigatória para criar um novo registro.")
                    .errorCode("required")
                    .build()));
        }

        CarWashRegistration entity = mapper.toEntity(request);
        entity.setPassword(passwordEncoder.encode(request.password()));
        entity.setDeletedAt(null);

        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    private void validateRequest(CarWashRegistrationRequest request, UUID existingId) {
        List<ValidationError> errors = new ArrayList<>();
        boolean isCreating = existingId == null;

        if (request.email() == null || request.email().isBlank() || !isValidEmail(request.email())) {
            errors.add(buildError("email", "Formato de email inválido", "invalid_email"));
        } else {
            repository.findByEmail(request.email()).ifPresent(user -> {
                if (isCreating || !Objects.equals(user.getId(), existingId)) {
                    errors.add(buildError("email", "Este e-mail já está em uso", "duplicate_email"));
                }
            });
        }

        if (request.cnpj() != null && !request.cnpj().isBlank()) {
            if (!isValidCnpj(request.cnpj())) {
                errors.add(buildError("cnpj", "Formato de CNPJ inválido", "invalid_cnpj"));
            } else {
                repository.findByCnpj(request.cnpj()).ifPresent(user -> {
                    if (isCreating || !Objects.equals(user.getId(), existingId)) {
                        errors.add(buildError("cnpj", "Este CNPJ já está em uso", "duplicate_cnpj"));
                    }
                });
            }
        }

        if (request.cpf() != null && !request.cpf().isBlank()) {
            if (!isValidCpf(request.cpf())) {
                errors.add(buildError("cpf", "Formato de CPF inválido", "invalid_cpf"));
            } else {
                repository.findByCpf(request.cpf()).ifPresent(user -> {
                    if (isCreating || !Objects.equals(user.getId(), existingId)) {
                        errors.add(buildError("cpf", "Este CPF já está em uso", "duplicate_cpf"));
                    }
                });
            }
        }

        if (!errors.isEmpty()) {
            throw new CustomValidationException(errors);
        }
    }

    // Método auxiliar para criar erros de validação
    private ValidationError buildError(String field, String message, String errorCode) {
        return ValidationError.builder()
                .field(field)
                .message(message)
                .errorCode(errorCode)
                .build();
    }

    private boolean isValidCnpj(String cnpj) {
        String cleanedCnpj = cnpj.replaceAll("\\D", "");
        return cleanedCnpj.length() == 14;
    }

    private boolean isValidCpf(String cpf) {
        String cleanedCpf = cpf.replaceAll("\\D", "");
        return cleanedCpf.length() == 11;
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[\\w\\-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
}