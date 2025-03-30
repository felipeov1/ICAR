package com.icar.plataform.repository;

import com.icar.plataform.domain.model.CarWashClosure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface CarWashClosureRepository extends JpaRepository<CarWashClosure, UUID> {
    List<CarWashClosure> findByCarWashIdAndClosureDateBetween(UUID carWashId, LocalDate start, LocalDate end);
    boolean existsByCarWashIdAndClosureDate(UUID carWashId, LocalDate closureDate);
}
