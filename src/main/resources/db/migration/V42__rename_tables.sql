

ALTER TABLE public.car_wash_appointment
DROP CONSTRAINT fk_car_wash_appointment_offering,
ADD CONSTRAINT fk_car_wash_appointment_offering
    FOREIGN KEY (offering_id)
    REFERENCES public.car_wash_profile_offering (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE;

CREATE INDEX IF NOT EXISTS idx_car_wash_profile_offering_profile_id
    ON public.car_wash_profile_offering USING btree
    (profile_id ASC NULLS LAST)
    TABLESPACE pg_default;

CREATE INDEX IF NOT EXISTS idx_offering_vehicle_details_vehicle_type
    ON public.car_wash_offering_vehicle_details USING btree
    (vehicle_type ASC NULLS LAST)
    TABLESPACE pg_default;

DROP TABLE public.car_wash_profile_offering_old CASCADE;
DROP TABLE public.car_wash_offering_vehicle_details_old;