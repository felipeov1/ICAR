CREATE TABLE IF NOT EXISTS public.advertisements
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    car_wash_profile_id uuid,
    title character varying(255) COLLATE pg_catalog."default" NOT NULL,
    description character varying(1000) COLLATE pg_catalog."default" NOT NULL,
    image_url character varying(255) COLLATE pg_catalog."default" NOT NULL,
    link_url character varying(255) COLLATE pg_catalog."default" NOT NULL,
    is_active boolean NOT NULL DEFAULT true,
    is_platform_ad boolean NOT NULL DEFAULT false,
    created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP,
    expires_at timestamp(6) without time zone NOT NULL,
    CONSTRAINT advertisements_pkey PRIMARY KEY (id),
    CONSTRAINT fk_advertisement_car_wash_profile FOREIGN KEY (car_wash_profile_id)
        REFERENCES public.car_wash_profile (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE SET NULL
);