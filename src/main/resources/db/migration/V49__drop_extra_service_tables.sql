DROP TABLE IF EXISTS public.offering_extra_services;

DROP TABLE IF EXISTS public.appointment_selected_extras;

ALTER TABLE public.car_wash_profile_offering
ADD COLUMN service_type VARCHAR(50) NOT NULL DEFAULT 'PRINCIPAL';

CREATE INDEX IF NOT EXISTS idx_offering_service_type
ON public.car_wash_profile_offering(service_type);