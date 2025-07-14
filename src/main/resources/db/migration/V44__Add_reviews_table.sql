CREATE TABLE IF NOT EXISTS public.reviews
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    profile_id uuid NOT NULL,
    customer_id uuid NOT NULL,
    appointment_id uuid NOT NULL,
    rating integer NOT NULL,
    comment text COLLATE pg_catalog."default",
    created_at timestamp(6) without time zone NOT NULL,
    CONSTRAINT reviews_pkey PRIMARY KEY (id),
    CONSTRAINT reviews_appointment_id_key UNIQUE (appointment_id),
    CONSTRAINT fk_reviews_appointment FOREIGN KEY (appointment_id)
        REFERENCES public.car_wash_appointment (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,

    CONSTRAINT fk_reviews_customer FOREIGN KEY (customer_id)
        REFERENCES public.customer (id) MATCH SIMPLE -- DE 'customers' PARA 'customer'
        ON UPDATE NO ACTION
        ON DELETE CASCADE,

    CONSTRAINT fk_reviews_profile FOREIGN KEY (profile_id)
        REFERENCES public.car_wash_profile (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT reviews_rating_check CHECK (rating >= 1 AND rating <= 5)
);