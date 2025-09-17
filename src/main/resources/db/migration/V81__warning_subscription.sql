CREATE TABLE dry_wash_subscriptions (
    id UUID PRIMARY KEY NOT NULL,
    customer_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_subscription_customer FOREIGN KEY(customer_id) REFERENCES customers(id),
    CONSTRAINT uk_customer_id UNIQUE (customer_id)
);