CREATE TABLE IF NOT EXISTS public.company_mercado_pago_config
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    company_id uuid NOT NULL,
    access_token character varying(255) COLLATE pg_catalog."default" NOT NULL,
    public_key character varying(255) COLLATE pg_catalog."default" NOT NULL,
    created_at timestamp(6) without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp(6) without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT company_mercado_pago_config_pkey PRIMARY KEY (id),
    CONSTRAINT uk_company_mercado_pago_config_company_id UNIQUE (company_id),
    CONSTRAINT fk_company_mercado_pago_config_registration FOREIGN KEY (company_id)
        REFERENCES public.car_wash_registration (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);


CREATE INDEX IF NOT EXISTS idx_company_mercado_pago_config_company_id
    ON public.company_mercado_pago_config USING btree
    (company_id ASC NULLS LAST);