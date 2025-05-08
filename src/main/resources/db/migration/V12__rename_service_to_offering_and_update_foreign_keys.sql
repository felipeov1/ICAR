ALTER TABLE public.appointment
    RENAME COLUMN service_id TO offering_id;

ALTER TABLE public.appointment
    DROP CONSTRAINT appointment_service_id_fkey;

ALTER TABLE public.appointment
    ADD CONSTRAINT appointment_offering_id_fkey FOREIGN KEY (offering_id)
        REFERENCES public.car_wash_offering (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION;

ALTER TABLE public.appointment
    ALTER COLUMN offering_id SET NOT NULL;  -- Ajuste conforme necessário