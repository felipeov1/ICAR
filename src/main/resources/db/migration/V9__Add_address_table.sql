CREATE TABLE IF NOT EXISTS public.customer_address (
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    customer_id uuid NOT NULL,
    address_name varchar(255) NOT NULL,
    zip_code varchar(20) NOT NULL,
    city varchar(100) NOT NULL,
    state varchar(100) NOT NULL,
    street varchar(255) NOT NULL,
    number varchar(20) NOT NULL,
    additional_instructions text,
    created_at timestamp DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT customer_address_pkey PRIMARY KEY (id),
    CONSTRAINT fk_customer
        FOREIGN KEY (customer_id)
        REFERENCES public.customer (id)
        ON DELETE CASCADE
);

