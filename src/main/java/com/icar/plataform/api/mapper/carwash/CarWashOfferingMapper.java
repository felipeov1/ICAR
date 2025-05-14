package com.icar.plataform.api.mapper.carwash;

import com.icar.plataform.api.dto.request.carwash.profile.CarWashOfferingRequest;
import com.icar.plataform.api.dto.response.carwash.profile.CarWashOfferingResponse;
import com.icar.plataform.domain.model.carwash.offering.CarWashOffering;
import com.icar.plataform.domain.enums.CarWashOfferingModality;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CarWashOfferingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "estimatedTime", source = "estimatedTime")
    @Mapping(target = "modality", source = "modality")  // Este mapeamento garante que o enum seja mapeado
    CarWashOffering toEntity(CarWashOfferingRequest dto);

    @Mapping(target = "statusText", expression = "java(entity.isActive() ? \"Ativo\" : \"Desativado\")")
    @Mapping(target = "modalityText", expression = "java(getModalityText(entity.getModality()))")
    @Mapping(target = "formattedPrice", expression = "java(formatPrice(entity.getPrice()))")
    @Mapping(target = "formattedTime", expression = "java(formatEstimatedTime(entity.getEstimatedTime()))")
    @Mapping(target = "estimatedTime", source = "entity.estimatedTime")
    CarWashOfferingResponse toDto(CarWashOffering entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "estimatedTime", source = "estimatedTime")
    @Mapping(target = "modality", source = "modality")  // Mapeando também no método de atualização
    void updateEntity(CarWashOfferingRequest dto, @MappingTarget CarWashOffering entity);

    default String formatPrice(BigDecimal price) {
        if (price == null) {
            return "";
        }
        Locale locale = new Locale("pt", "BR");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
        return currencyFormat.format(price);
    }

    default String formatEstimatedTime(Integer estimatedTime) {
        if (estimatedTime == null) {
            return "";
        }
        int hours = estimatedTime / 60;
        int minutes = estimatedTime % 60;
        if (hours > 0) {
            return String.format("%d hora(s) %d minuto(s)", hours, minutes);
        } else {
            return String.format("%d minuto(s)", minutes);
        }
    }

    default String getModalityText(CarWashOfferingModality modality) {
        if (CarWashOfferingModality.IN_PERSON.equals(modality)) {
            return "Na empresa";
        } else if (CarWashOfferingModality.AT_HOME.equals(modality)) {
            return "Domiciliar";
        }
        return "";
    }
}
