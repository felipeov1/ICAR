CREATE TABLE IF NOT EXISTS public.car_wash_profile_offering_new
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    active boolean NOT NULL DEFAULT true,
    created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP,
    description text COLLATE pg_catalog."default",
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP,
    profile_id uuid NOT NULL,
    deleted_at timestamp with time zone,
    CONSTRAINT car_wash_profile_offering_new_pkey PRIMARY KEY (id),
    CONSTRAINT fk_car_wash_profile_offering_new_profile FOREIGN KEY (profile_id)
        REFERENCES public.car_wash_profile (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)
TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS public.car_wash_offering_vehicle_details_new
(
    offering_id uuid NOT NULL,
    vehicle_type character varying(100) COLLATE pg_catalog."default" NOT NULL,
    price numeric(10,2) NOT NULL,
    estimated_time integer NOT NULL,
    CONSTRAINT car_wash_offering_vehicle_details_new_pkey PRIMARY KEY (offering_id, vehicle_type),
    CONSTRAINT fk_offering_vehicle_details_new_offering FOREIGN KEY (offering_id)
        REFERENCES public.car_wash_profile_offering_new (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)
TABLESPACE pg_default;