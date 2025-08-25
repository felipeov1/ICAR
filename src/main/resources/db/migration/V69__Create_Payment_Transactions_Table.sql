
DROP TABLE IF EXISTS public.payment_transactions CASCADE;


CREATE TABLE public.payment_transactions
(
    id                      uuid NOT NULL DEFAULT gen_random_uuid(),
    mercado_pago_payment_id bigint NOT NULL,
    status                  character varying(50) NOT NULL,
    appointment_id          uuid NOT NULL,
    created_at              timestamp(6) without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              timestamp(6) without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT payment_transactions_pkey PRIMARY KEY (id),
    CONSTRAINT uk_mercado_pago_payment_id UNIQUE (mercado_pago_payment_id),

    CONSTRAINT fk_appointment_transaction FOREIGN KEY (appointment_id)
        REFERENCES public.car_wash_appointment (id) ON DELETE CASCADE
);

COMMENT ON TABLE public.payment_transactions
    IS 'Stores transaction details for payments processed via Mercado Pago, linked to a specific appointment.';

CREATE INDEX idx_payment_transactions_appointment_id
    ON public.payment_transactions (appointment_id);
