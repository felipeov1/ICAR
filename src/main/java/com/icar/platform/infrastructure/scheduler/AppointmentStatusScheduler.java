package com.icar.platform.infrastructure.scheduler;

import com.icar.platform.domain.enums.AppointmentStatus;
import com.icar.platform.domain.model.appointment.CarWashAppointment;
import com.icar.platform.domain.repository.appointment.CarWashAppointmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppointmentStatusScheduler {

    private final CarWashAppointmentRepository appointmentRepository;

    @Scheduled(cron = "0 */5 * * * *")
    @Transactional
    public void autoCompleteAppointments() {
        log.info("SCHEDULER: Iniciando verificação de agendamentos para completar...");
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime lookbackTime = now.minusHours(48);

        List<CarWashAppointment> candidates = appointmentRepository.findCompletableAppointments(lookbackTime, now);

        if (candidates.isEmpty()) {
            log.info("SCHEDULER: Nenhum agendamento recente para verificar. Tarefa finalizada.");
            return;
        }

        log.info("SCHEDULER: {} agendamentos candidatos encontrados. Verificando horários de término...", candidates.size());
        int completedCount = 0;

        for (CarWashAppointment appointment : candidates) {
            Integer estimatedTime = appointment.getTotalDurationMinutes();

            if (estimatedTime == null || estimatedTime <= 0) {
                log.warn("SCHEDULER: Duração total inválida para o agendamento ID {}. Pulando.", appointment.getId());
                continue;
            }

            ZonedDateTime endTime = appointment.getDateTime().plusMinutes(estimatedTime);

            if (now.isAfter(endTime)) {
                appointment.setStatus(AppointmentStatus.COMPLETED);
                appointment.setUpdatedAt(now);
                completedCount++;
                log.info("SCHEDULER: Agendamento ID {} foi marcado como COMPLETED automaticamente.", appointment.getId());
            }
        }

        if (completedCount > 0) {
            log.info("SCHEDULER: Tarefa finalizada. {} agendamentos foram completados.", completedCount);
        } else {
            log.info("SCHEDULER: Tarefa finalizada. Nenhum agendamento atingiu a data de conclusão ainda.");
        }
    }
}