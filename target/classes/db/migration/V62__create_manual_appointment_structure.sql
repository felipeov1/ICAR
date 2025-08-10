CREATE TYPE appointment_creation_channel AS ENUM ('MARKETPLACE', 'MANUAL');

ALTER TABLE car_wash_appointment
ADD COLUMN creation_channel appointment_creation_channel NOT NULL DEFAULT 'MARKETPLACE';

ALTER TABLE car_wash_appointment
ALTER COLUMN customer_id DROP NOT NULL;

ALTER TABLE customer_address
ALTER COLUMN customer_id DROP NOT NULL;

CREATE TABLE company_customer (
    id UUID PRIMARY KEY,
    profile_id UUID NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    zip_code VARCHAR(10),
    street VARCHAR(255),
    street_number VARCHAR(20),
    neighborhood VARCHAR(100),
    city VARCHAR(100),
    state VARCHAR(2),
    additional_instructions VARCHAR(500),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    deleted_at TIMESTAMPTZ,
    CONSTRAINT fk_company_customer_profile FOREIGN KEY (profile_id) REFERENCES car_wash_profile(id)
);

CREATE INDEX idx_company_customer_profile_id ON company_customer(profile_id);
