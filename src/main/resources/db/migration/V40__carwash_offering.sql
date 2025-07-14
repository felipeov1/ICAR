-- Drop old tables if they exist
DROP TABLE IF EXISTS public.car_wash_offering_vehicle_prices;
DROP TABLE IF EXISTS public.car_wash_offering_vehicle_times;

-- Create a single table for both price and time
CREATE TABLE IF NOT EXISTS public.car_wash_offering_vehicle_details (
    offering_id uuid NOT NULL,
    vehicle_type character varying(100) COLLATE pg_catalog."default" NOT NULL,
    price numeric(10,2) NOT NULL,
    estimated_time integer NOT NULL,
    CONSTRAINT car_wash_offering_vehicle_details_pkey PRIMARY KEY (offering_id, vehicle_type),
    CONSTRAINT fk_offering_vehicle_details_offering FOREIGN KEY (offering_id)
        REFERENCES public.car_wash_profile_offering (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);