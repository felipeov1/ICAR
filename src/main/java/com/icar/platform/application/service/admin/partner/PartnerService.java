package com.icar.platform.application.service.admin.partner;

import com.icar.platform.api.dto.request.admin.PartnerCreateRequest;
import com.icar.platform.api.dto.request.admin.PartnerUpdateRequest;
import com.icar.platform.api.dto.response.admin.PartnerResponse;
import com.icar.platform.api.mapper.admin.PartnerMapper;
import com.icar.platform.domain.enums.SubscriptionStatus;
import com.icar.platform.domain.model.admin.Plan;
import com.icar.platform.domain.model.admin.Subscription;
import com.icar.platform.domain.model.carwash.legal.CarWashRegistration;
import com.icar.platform.domain.repository.admin.PlanRepository;
import com.icar.platform.domain.repository.admin.SubscriptionRepository;
import com.icar.platform.domain.repository.carwash.legal.CarWashRegistrationDataRepository;
import com.icar.platform.shared.exception.BusinessException;
import com.icar.platform.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartnerService {

    private final CarWashRegistrationDataRepository partnerRepository;
    private final PlanRepository planRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PartnerMapper partnerMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<PartnerResponse> findAllPartners() {
        return partnerRepository.findAllWithSubscriptions().stream()
                .map(partnerMapper::toPartnerResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PartnerResponse createPartner(PartnerCreateRequest request) {
        if (partnerRepository.findByEmail(request.email()).isPresent()) {
            throw new BusinessException("E-mail já cadastrado.");
        }
        CarWashRegistration partner = new CarWashRegistration();
        partner.setTradeName(request.tradeName());
        partner.setOwnerName(request.ownerName());
        partner.setCnpj(request.cnpj());
        partner.setPhone(request.phone());
        partner.setEmail(request.email());
        partner.setPassword(passwordEncoder.encode(request.password()));
        partner.setZipCode(request.zipCode());
        partner.setCity(request.city());
        partner.setState(request.state());
        CarWashRegistration savedPartner = partnerRepository.save(partner);
        createOrUpdateSubscription(savedPartner, request.planId(), request.joinDate().atStartOfDay());
        return partnerMapper.toPartnerResponse(partnerRepository.findById(savedPartner.getId()).get());
    }

    @Transactional
    public PartnerResponse updatePartner(UUID id, PartnerUpdateRequest request) {
        CarWashRegistration partner = partnerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parceiro não encontrado."));

        partner.setTradeName(request.tradeName());
        partner.setOwnerName(request.ownerName());
        partner.setCnpj(request.cnpj());
        partner.setPhone(request.phone());
        partner.setEmail(request.email());
        partner.setZipCode(request.zipCode());
        partner.setCity(request.city());
        partner.setState(request.state());

        partner.setCreatedAt(request.joinDate().atStartOfDay());

        createOrUpdateSubscription(partner, request.planId(), request.joinDate().atStartOfDay());

        CarWashRegistration updatedPartner = partnerRepository.save(partner);
        return partnerMapper.toPartnerResponse(updatedPartner);
    }

    @Transactional
    public void deactivatePartner(UUID id) {
        CarWashRegistration partner = partnerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parceiro não encontrado."));
        partner.setDeletedAt(LocalDateTime.now());
        partner.getSubscriptions().stream()
                .filter(s -> s.getStatus() == SubscriptionStatus.ACTIVE)
                .forEach(s -> s.setStatus(SubscriptionStatus.CANCELED));
        partnerRepository.save(partner);
    }

    private void createOrUpdateSubscription(CarWashRegistration partner, UUID planId, LocalDateTime startDate) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plano não encontrado."));
        partner.getSubscriptions().stream()
                .filter(s -> s.getStatus() == SubscriptionStatus.ACTIVE)
                .forEach(s -> s.setStatus(SubscriptionStatus.CANCELED));
        Subscription newSubscription = new Subscription();
        newSubscription.setCarWashRegistration(partner);
        newSubscription.setPlan(plan);
        newSubscription.setStatus(SubscriptionStatus.ACTIVE);
        newSubscription.setStartDate(startDate);
        LocalDateTime endDate;
        if (plan.getDurationInDays() != null) {
            endDate = startDate.plusDays(plan.getDurationInDays());
        } else {
            endDate = startDate.plus(plan.getBillingFrequency(), plan.getBillingPeriod());
        }
        newSubscription.setEndDate(endDate);
        partner.getSubscriptions().add(newSubscription);
    }

    @Transactional
    public PartnerResponse renewSubscriptionForPartner(UUID partnerId) {
        CarWashRegistration partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Parceiro não encontrado."));
        Subscription lastSubscription = partner.getSubscriptions().stream()
                .max(Comparator.comparing(Subscription::getEndDate))
                .orElseThrow(() -> new BusinessException("Nenhuma assinatura anterior encontrada para renovar."));
        Plan planToRenew = lastSubscription.getPlan();
        partner.getSubscriptions().stream()
                .filter(s -> s.getStatus() == SubscriptionStatus.ACTIVE)
                .forEach(s -> s.setStatus(SubscriptionStatus.CANCELED));
        Subscription newSubscription = new Subscription();
        newSubscription.setCarWashRegistration(partner);
        newSubscription.setPlan(planToRenew);
        newSubscription.setStatus(SubscriptionStatus.ACTIVE);
        LocalDateTime startDate = LocalDateTime.now();
        newSubscription.setStartDate(startDate);
        LocalDateTime endDate;
        if (planToRenew.getDurationInDays() != null) {
            endDate = startDate.plusDays(planToRenew.getDurationInDays());
        } else {
            endDate = startDate.plus(planToRenew.getBillingFrequency(), planToRenew.getBillingPeriod());
        }
        newSubscription.setEndDate(endDate);
        partner.getSubscriptions().add(newSubscription);
        partnerRepository.save(partner);
        return partnerMapper.toPartnerResponse(partner);
    }
}