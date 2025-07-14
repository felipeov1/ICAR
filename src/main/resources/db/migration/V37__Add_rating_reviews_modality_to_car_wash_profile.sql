ALTER TABLE public.car_wash_profile
ADD COLUMN rating NUMERIC(3, 2) NOT NULL DEFAULT 0.0 CHECK (rating >= 0.0 AND rating <= 5.0),
ADD COLUMN reviews INTEGER NOT NULL DEFAULT 0 CHECK (reviews >= 0);

ALTER TABLE public.car_wash_profile
ADD COLUMN modalities TEXT[];