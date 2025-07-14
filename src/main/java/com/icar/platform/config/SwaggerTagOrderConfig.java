package com.icar.platform.config;

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
                // ========== CAR WASH PROFILE ==========
                new Tag().name("Car Wash Profile - General")
                        .description("Manage basic profile info of the car wash"),

                new Tag().name("Car Wash Profile - Offerings")
                        .description("Manage car wash services and offerings"),

                new Tag().name("Car Wash Profile - Photos")
                        .description("Manage profile photos of the car wash"),

                // --- Schedule Section ---
                new Tag().name("Car Wash Profile - Schedule - Weekly")
                        .description("Define weekly work schedule"),

                new Tag().name("Car Wash Profile - Schedule - Special Days")
                        .description("Configure exceptions and special dates"),

                new Tag().name("Car Wash Profile - Schedule - Availability")
                        .description("Define available time slots"),

                new Tag().name("Car Wash Profile - Appointment Config")
                        .description("Configure appointment rules and logic"),

                // ========== APPOINTMENTS ==========
                new Tag().name("Car Wash - Appointments")
                        .description("Manage car wash appointments from establishment perspective"),

                new Tag().name("Customer - Appointments")
                        .description("Manage customer appointments from user perspective"),

                new Tag().name("Coupons Management")
                        .description("Apply coupons."),

                new Tag().name("Applied Coupons")
                        .description("Manage and track coupons applied by customers, including usage history and validation status."),

                // ========== LEGAL INFORMATION ==========
                new Tag().name("Car Wash - Legal Information")
                        .description("Legal and operational details of the establishment"),

                // ========== CUSTOMER MANAGEMENT ==========
                new Tag().name("Customer Settings - Profile")
                        .description("Manage customer profile information"),

                new Tag().name("Customer Settings - Addresses")
                        .description("Manage customer address records"),

                // ========== AUTHENTICATION ==========
                new Tag().name("Customer Auth")
                        .description("Handles login, registration, and account security"),


                // ========== SYSTEM ==========
                new Tag().name("Health Check")
                        .description("Returns basic status and system health information")

        ));
    }
}