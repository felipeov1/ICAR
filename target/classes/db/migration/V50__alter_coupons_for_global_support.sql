ALTER TABLE public.coupons
ALTER COLUMN profile_id DROP NOT NULL;

ALTER TABLE public.coupons
DROP CONSTRAINT IF EXISTS fk_coupons_profile;