package com.icar.plataform.service.validation;

import com.icar.plataform.exception.BusinessException;
import com.icar.plataform.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class PromotionValidator {

    private final PromotionRepository repository;

    public void validatePromotionPeriod(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            throw new BusinessException("Período de promoção inválido");
        }
    }
}
