CREATE TABLE IF NOT EXISTS public.car_wash_profile_offering_extra_services (
    main_offering_id UUID NOT NULL,
    extra_service_id UUID NOT NULL,
    CONSTRAINT pk_offering_extra_services PRIMARY KEY (main_offering_id, extra_service_id),
    CONSTRAINT fk_main_offering FOREIGN KEY (main_offering_id)
        REFERENCES public.car_wash_profile_offering (id) ON DELETE CASCADE,
    CONSTRAINT fk_extra_service FOREIGN KEY (extra_service_id)
        REFERENCES public.car_wash_profile_offering (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.car_wash_profile_appointment_selected_extras (
    appointment_id UUID NOT NULL,
    extra_service_id UUID NOT NULL,
    CONSTRAINT pk_appointment_selected_extras PRIMARY KEY (appointment_id, extra_service_id),
    CONSTRAINT fk_appointment FOREIGN KEY (appointment_id)
        REFERENCES public.car_wash_appointment (id) ON DELETE CASCADE,
    CONSTRAINT fk_extra_service FOREIGN KEY (extra_service_id)
        REFERENCES public.car_wash_profile_offering (id) ON DELETE CASCADE
);