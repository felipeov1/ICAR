DROP TABLE IF EXISTS 
    car_wash_appointment,
    car_wash_profile_special_days,
    car_wash_profile_weekly_schedule,
    car_wash_profile_vehicle_types,
    car_wash_profile_photos,
    car_wash_profile_offering,
    car_wash_profile,
    car_wash_registration
CASCADE;

CREATE TABLE IF NOT EXISTS public.car_wash_registration
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    address character varying(255) COLLATE pg_catalog."default" NOT NULL,
    cnpj character varying(14) COLLATE pg_catalog."default",
    cpf character varying(11) COLLATE pg_catalog."default",
    created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP,
    deleted_at timestamp(6) without time zone,
    email character varying(255) COLLATE pg_catalog."default" NOT NULL,
    legal_name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    owner_name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    phone character varying(15) COLLATE pg_catalog."default" NOT NULL,
    trade_name character varying(255) COLLATE pg_catalog."default",
    updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP,
    subdomain character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT car_wash_registration_pkey PRIMARY KEY (id),
    CONSTRAINT uk_car_wash_registration_subdomain UNIQUE (subdomain)
)

TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS public.car_wash_profile
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    car_wash_id uuid NOT NULL,
    cover_photo character varying(255) COLLATE pg_catalog."default",
    created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP,
    description character varying(1000) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP,
    active boolean DEFAULT true,
    slug character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT car_wash_profile_pkey PRIMARY KEY (id),
    CONSTRAINT fk_car_wash_profile_registration FOREIGN KEY (car_wash_id)
        REFERENCES public.car_wash_registration (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT uk_car_wash_profile_slug UNIQUE (slug)
)

TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS public.car_wash_profile_offering
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    active boolean NOT NULL DEFAULT true,
    created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP,
    description text COLLATE pg_catalog."default",
    estimated_time integer NOT NULL,
    modality character varying(255) COLLATE pg_catalog."default" NOT NULL,
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    price numeric(38,2) NOT NULL,
    updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP,
    profile_id uuid NOT NULL,
    CONSTRAINT car_wash_profile_offering_pkey PRIMARY KEY (id),
    CONSTRAINT fk_car_wash_profile_offering_profile FOREIGN KEY (profile_id)
        REFERENCES public.car_wash_profile (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT car_wash_profile_offering_modality_check CHECK (modality::text = ANY (ARRAY['IN_PERSON'::character varying::text, 'AT_HOME'::character varying::text]))
)

TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS public.car_wash_profile_photos
(
    profile_id uuid NOT NULL,
    photo_url character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT car_wash_profile_photos_pkey PRIMARY KEY (profile_id, photo_url),
    CONSTRAINT fk_car_wash_profile_photos_profile FOREIGN KEY (profile_id)
        REFERENCES public.car_wash_profile (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)

TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS public.car_wash_profile_vehicle_types
(
    profile_id uuid NOT NULL,
    vehicle_type character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT car_wash_profile_vehicle_types_pkey PRIMARY KEY (profile_id, vehicle_type),
    CONSTRAINT fk_car_wash_profile_vehicle_types_profile FOREIGN KEY (profile_id)
        REFERENCES public.car_wash_profile (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)

TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS public.car_wash_profile_weekly_schedule
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    profile_id uuid NOT NULL,
    day_of_week character varying(255) COLLATE pg_catalog."default" NOT NULL,
    start_time time without time zone NOT NULL,
    end_time time without time zone NOT NULL,
    available boolean NOT NULL DEFAULT true,
    appointment_interval_minutes integer NOT NULL,
    CONSTRAINT car_wash_profile_weekly_schedule_pkey PRIMARY KEY (id),
    CONSTRAINT fk_car_wash_profile_weekly_schedule_profile FOREIGN KEY (profile_id)
        REFERENCES public.car_wash_profile (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)

TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS public.car_wash_profile_special_days
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    profile_id uuid NOT NULL,
    date date NOT NULL,
    start_time time without time zone,
    end_time time without time zone,
    is_holiday boolean NOT NULL DEFAULT false,
    reason character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT car_wash_profile_special_days_pkey PRIMARY KEY (id),
    CONSTRAINT fk_car_wash_profile_special_days_profile FOREIGN KEY (profile_id)
        REFERENCES public.car_wash_profile (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)

TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS public.car_wash_appointment
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    customer_id uuid,
    profile_id uuid NOT NULL,
    offering_id uuid NOT NULL,
    date_time timestamp without time zone NOT NULL,
    status character varying(20) COLLATE pg_catalog."default" DEFAULT 'PENDING'::character varying,
    payment_method character varying(50) COLLATE pg_catalog."default",
    amount_paid numeric(10,2),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    deleted_at timestamp without time zone,
    CONSTRAINT car_wash_appointment_pkey PRIMARY KEY (id),
    CONSTRAINT fk_car_wash_appointment_offering FOREIGN KEY (offering_id)
        REFERENCES public.car_wash_profile_offering (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_car_wash_appointment_profile FOREIGN KEY (profile_id)
        REFERENCES public.car_wash_profile (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)

TABLESPACE pg_default;

CREATE INDEX IF NOT EXISTS idx_car_wash_profile_car_wash_id
    ON public.car_wash_profile USING btree
    (car_wash_id ASC NULLS LAST)
    TABLESPACE pg_default;

CREATE INDEX IF NOT EXISTS idx_car_wash_profile_slug
    ON public.car_wash_profile USING btree
    (slug COLLATE pg_catalog."default" ASC NULLS LAST)
    TABLESPACE pg_default;

CREATE INDEX IF NOT EXISTS idx_car_wash_profile_offering_profile_id
    ON public.car_wash_profile_offering USING btree
    (profile_id ASC NULLS LAST)
    TABLESPACE pg_default;

CREATE INDEX IF NOT EXISTS idx_car_wash_appointment_profile_id
    ON public.car_wash_appointment USING btree
    (profile_id ASC NULLS LAST)
    TABLESPACE pg_default;

CREATE INDEX IF NOT EXISTS idx_car_wash_appointment_date_time
    ON public.car_wash_appointment USING btree
    (date_time ASC NULLS LAST)
    TABLESPACE pg_default;

CREATE INDEX IF NOT EXISTS idx_car_wash_profile_weekly_schedule_profile_id
    ON public.car_wash_profile_weekly_schedule USING btree
    (profile_id ASC NULLS LAST)
    TABLESPACE pg_default;

CREATE INDEX IF NOT EXISTS idx_car_wash_profile_special_days_profile_id
    ON public.car_wash_profile_special_days USING btree
    (profile_id ASC NULLS LAST)
    TABLESPACE pg_default;

CREATE INDEX IF NOT EXISTS idx_car_wash_profile_special_days_date
    ON public.car_wash_profile_special_days USING btree
    (date ASC NULLS LAST)
    TABLESPACE pg_default;