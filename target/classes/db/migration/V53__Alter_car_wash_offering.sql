CREATE TABLE IF NOT EXISTS public.car_wash_profile_offering
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    active boolean NOT NULL DEFAULT true,
    created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP,
    description text COLLATE pg_catalog."default",
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP,
    profile_id uuid NOT NULL,
    deleted_at timestamp with time zone,
    service_type character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT car_wash_profile_offering_pkey PRIMARY KEY (id),
    CONSTRAINT fk_car_wash_profile_offering_profile FOREIGN KEY (profile_id)
        REFERENCES public.car_wash_profile (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS public.car_wash_appointment_selected_services
(
    appointment_id uuid NOT NULL,
    service_id uuid NOT NULL,
    CONSTRAINT car_wash_appointment_selected_services_pkey PRIMARY KEY (appointment_id, service_id),
    CONSTRAINT fk_appointment_id FOREIGN KEY (appointment_id)
        REFERENCES public.car_wash_appointment (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT fk_offering_id FOREIGN KEY (service_id)
        REFERENCES public.car_wash_profile_offering (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);