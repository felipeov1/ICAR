package com.icar.plataform.api.mapper.carwash;

import com.icar.plataform.api.dto.request.carwash.CarWashOfferingRequest;
import com.icar.plataform.api.dto.response.carwash.CarWashOfferingResponse;
import com.icar.plataform.domain.enums.CarWashOfferingModality;
import com.icar.plataform.domain.model.carwash.CarWashOffering;
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
    CarWashOffering toEntity(CarWashOfferingRequest dto);

    @Mapping(target = "statusText", expression = "java(entity.getActive() != null && entity.getActive() ? \"Ativo\" : \"Desativado\")")
    @Mapping(target = "modalityText", expression = "java(getModalityText(entity.getModality()))")
    @Mapping(target = "formattedPrice", expression = "java(formatPrice(entity.getPrice()))")
    @Mapping(target = "formattedTime", expression = "java(formatEstimatedTime(entity.getEstimatedTime()))")
    @Mapping(target = "estimatedTime", source = "entity.estimatedTime")
    CarWashOfferingResponse toDto(CarWashOffering entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "estimatedTime", source = "estimatedTime")
    void updateEntity(CarWashOfferingRequest dto, @MappingTarget CarWashOffering entity);

    default String formatPrice(BigDecimal price) {
        if (price == null) {
            return "";
        }
        Locale locale = Locale.of("pt", "BR");
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
        if (modality == CarWashOfferingModality.IN_PERSON) {
            return "Na empresa";
        } else if (modality == CarWashOfferingModality.AT_HOME) {
            return "Domiciliar";
        }
        return "";
    }
}
