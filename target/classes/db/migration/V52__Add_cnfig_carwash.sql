ALTER TABLE public.car_wash_profile_appointment_config
ADD COLUMN gap_minutes INTEGER NOT NULL DEFAULT 15,
ADD COLUMN allow_overtime BOOLEAN NOT NULL DEFAULT false;

ALTER TABLE public.car_wash_appointment
ADD COLUMN total_duration_minutes INTEGER;