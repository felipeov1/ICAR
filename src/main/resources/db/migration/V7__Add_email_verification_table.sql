CREATE TABLE email_verification (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    verified_at TIMESTAMP NULL,
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);

ALTER TABLE customer
ADD COLUMN email_verified BOOLEAN NOT NULL DEFAULT FALSE;

