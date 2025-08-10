package com.icar.platform.domain.model.carwash.offering;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Embeddable
@Getter
@Setter
public class VehicleOfferingDetail {

    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "estimated_time", nullable = false)
    private Integer estimatedTime;
}