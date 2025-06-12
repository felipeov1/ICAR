CREATE TABLE IF NOT EXISTS public.coupons (
      id uuid NOT NULL DEFAULT gen_random_uuid(),
      code varchar(50) NOT NULL,
      profile_id uuid NOT NULL,
      discount_value numeric(10,2),
      discount_percentage numeric(5,2),
      valid_from timestamp without time zone NOT NULL,
      valid_until timestamp without time zone NOT NULL,
      max_uses integer,
      max_uses_per_user integer,
      current_uses integer DEFAULT 0,
      created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
      min_order_value numeric(10,2),
      CONSTRAINT coupons_pkey PRIMARY KEY (id),
      CONSTRAINT fk_coupons_profile FOREIGN KEY (profile_id)
          REFERENCES public.car_wash_profile (id),
      CONSTRAINT chk_coupon_discount CHECK (
          (discount_value IS NOT NULL AND discount_percentage IS NULL) OR
          (discount_value IS NULL AND discount_percentage IS NOT NULL)
      )
  );

CREATE TABLE IF NOT EXISTS public.applied_coupons (
    appointment_id uuid NOT NULL,
    coupon_id uuid NOT NULL,
    customer_id uuid NOT NULL,
    applied_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    discount_applied numeric(10,2) NOT NULL,
    CONSTRAINT applied_coupons_pkey PRIMARY KEY (appointment_id, coupon_id),
    CONSTRAINT fk_applied_coupons_appointment FOREIGN KEY (appointment_id)
        REFERENCES public.car_wash_appointment (id),
    CONSTRAINT fk_applied_coupons_coupon FOREIGN KEY (coupon_id)
        REFERENCES public.coupons (id),
    CONSTRAINT fk_applied_coupons_customer FOREIGN KEY (customer_id)
        REFERENCES public.customer (id)
);

CREATE INDEX IF NOT EXISTS idx_coupons_code ON public.coupons USING btree (code);