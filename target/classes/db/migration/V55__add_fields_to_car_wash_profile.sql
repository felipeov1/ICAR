ALTER TABLE public.car_wash_profile DROP COLUMN IF EXISTS price_range;

ALTER TABLE public.car_wash_profile ADD COLUMN IF NOT EXISTS subdomain VARCHAR(100);
ALTER TABLE public.car_wash_profile ADD COLUMN IF NOT EXISTS whatsapp VARCHAR(20);
ALTER TABLE public.car_wash_profile ADD COLUMN IF NOT EXISTS locations TEXT[];
ALTER TABLE public.car_wash_profile ADD COLUMN IF NOT EXISTS observations VARCHAR(500);

UPDATE public.car_wash_profile
SET subdomain = 'temp-' || gen_random_uuid()
WHERE subdomain IS NULL;

ALTER TABLE public.car_wash_profile ALTER COLUMN subdomain SET NOT NULL;

ALTER TABLE public.car_wash_profile DROP CONSTRAINT IF EXISTS uk_car_wash_profile_subdomain;

ALTER TABLE public.car_wash_profile ADD CONSTRAINT uk_car_wash_profile_subdomain UNIQUE (subdomain);