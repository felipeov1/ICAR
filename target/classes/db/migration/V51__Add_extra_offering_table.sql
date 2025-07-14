ALTER TABLE public.car_wash_profile_appointment_selected_extras
DROP CONSTRAINT IF EXISTS fk6n2r0i5yn6g30jq8tkd0jxqq9,
DROP CONSTRAINT IF EXISTS fk7qyfnea12qn1bwxbvlh10v1w1;

ALTER TABLE public.car_wash_profile_appointment_selected_extras
ADD CONSTRAINT fk_extras_appointment_id
    FOREIGN KEY (appointment_id)
    REFERENCES public.car_wash_appointment (id)
    ON DELETE CASCADE,
ADD CONSTRAINT fk_extras_offering_id
    FOREIGN KEY (extra_service_id)
    REFERENCES public.car_wash_profile_offering (id)
    ON DELETE CASCADE;