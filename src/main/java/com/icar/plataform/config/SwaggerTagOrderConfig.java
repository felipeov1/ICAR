package com.icar.plataform.config;

import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerTagOrderConfig {

    @Bean
    public OpenApiCustomizer customTagOrder() {
        return openApi -> openApi.setTags(List.of(
                // --- Car Wash Profile ---
                new Tag().name("Car Wash Profile - General").description("Manage basic profile info of the car wash"),
                new Tag().name("Car Wash Profile - Offerings").description("Manage car wash services and offerings"),
                new Tag().name("Car Wash Profile - Photos").description("Manage profile photos of the car wash"),
                new Tag().name("Car Wash Profile - Schedule - Weekly").description("Define weekly work schedule"),
                new Tag().name("Car Wash Profile - Schedule - Special Days").description("Configure exceptions and special dates"),
                new Tag().name("Car Wash Profile - Schedule - Availability").description("Define available time slots"),
                new Tag().name("Car Wash Profile - Appointment Config").description("Configure appointment rules and logic"),

                // --- Legal Information ---
                new Tag().name("Car Wash - Legal Information").description("Legal and operational details of the establishment"),

                // --- Customer Authentication & Settings ---
                new Tag().name("Customer Settings - Profile").description("Manage customer profile information"),
                new Tag().name("Customer Settings - Addresses").description("Manage customer address records"),
                new Tag().name("Customer Auth").description("Handles login, registration, and account security"),

                // --- System ---
                new Tag().name("Health Check").description("Returns basic status and system health information")
        ));
    }
}
