ALTER TABLE public.car_wash_profile
ADD COLUMN logo character varying(255);

ALTER TABLE public.car_wash_profile_offering
ADD COLUMN vehicle_type character varying(100) NOT NULL DEFAULT 'CAR';

CREATE TABLE IF NOT EXISTS public.car_wash_offering_vehicle_prices (
    offering_id uuid NOT NULL,
    vehicle_type character varying(100) NOT NULL,
    price numeric(10,2) NOT NULL,
    CONSTRAINT car_wash_offering_vehicle_prices_pkey PRIMARY KEY (offering_id, vehicle_type),
    CONSTRAINT fk_offering_vehicle_prices_offering FOREIGN KEY (offering_id)
        REFERENCES public.car_wash_profile_offering (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.car_wash_offering_vehicle_times (
    offering_id uuid NOT NULL,
    vehicle_type character varying(100) NOT NULL,
    estimated_time integer NOT NULL,
    CONSTRAINT car_wash_offering_vehicle_times_pkey PRIMARY KEY (offering_id, vehicle_type),
    CONSTRAINT fk_offering_vehicle_times_offering FOREIGN KEY (offering_id)
        REFERENCES public.car_wash_profile_offering (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);