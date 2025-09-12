package com.icar.platform.domain.repository.admin;

import com.icar.platform.domain.model.admin.MonthlyPlatformStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MonthlyPlatformStatsRepository extends JpaRepository<MonthlyPlatformStats, UUID> {
    Optional<MonthlyPlatformStats> findByYearAndMonth(int year, int month);
}