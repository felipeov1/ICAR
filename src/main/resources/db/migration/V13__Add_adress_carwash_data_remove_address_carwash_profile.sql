ALTER TABLE public.car_wash_profile
DROP COLUMN address;

ALTER TABLE public.car_wash
ADD COLUMN address TEXT;
