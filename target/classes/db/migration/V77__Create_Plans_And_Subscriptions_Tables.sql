
CREATE TABLE plans (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    billing_frequency INT NOT NULL,
    billing_period VARCHAR(255) NOT NULL,
    duration_in_days INT,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE subscriptions (
    id UUID PRIMARY KEY,
    car_wash_registration_id UUID NOT NULL,
    plan_id UUID NOT NULL,
    status VARCHAR(255) NOT NULL,
    start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date TIMESTAMP WITHOUT TIME ZONE,
    canceled_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT fk_subscriptions_car_wash FOREIGN KEY (car_wash_registration_id) REFERENCES car_wash_registration(id),
    CONSTRAINT fk_subscriptions_plan FOREIGN KEY (plan_id) REFERENCES plans(id)
);

CREATE INDEX idx_subscriptions_status ON subscriptions(status);