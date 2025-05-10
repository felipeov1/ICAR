ALTER TABLE public.car_wash_offering
    ADD COLUMN profile_id UUID NOT NULL;

ALTER TABLE public.car_wash_offering
    ADD CONSTRAINT fk_car_wash_offering_profile FOREIGN KEY (profile_id)
    REFERENCES public.car_wash_profile(id)
    ON DELETE CASCADE;

