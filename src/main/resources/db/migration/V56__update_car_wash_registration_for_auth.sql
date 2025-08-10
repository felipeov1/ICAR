ALTER TABLE public.car_wash_registration DROP COLUMN IF EXISTS subdomain;

ALTER TABLE public.car_wash_registration ADD COLUMN IF NOT EXISTS password VARCHAR(255);

UPDATE public.car_wash_registration
SET password = 'Dev10fs2022@'
WHERE password IS NULL;

ALTER TABLE public.car_wash_registration ALTER COLUMN password SET NOT NULL;