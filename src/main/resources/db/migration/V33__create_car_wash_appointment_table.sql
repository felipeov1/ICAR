CREATE TABLE IF NOT EXISTS car_wash_appointment (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_id UUID NOT NULL,
    profile_id UUID NOT NULL,
    offering_id UUID NOT NULL,
    date_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    payment_method VARCHAR(50),
    amount_paid NUMERIC(10,2),
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT fk_appointment_customer FOREIGN KEY (customer_id) REFERENCES customer(id),
    CONSTRAINT fk_appointment_profile FOREIGN KEY (profile_id) REFERENCES car_wash_profile(id),
    CONSTRAINT fk_appointment_offering FOREIGN KEY (offering_id) REFERENCES car_wash_profile_offering(id)
);

CREATE INDEX idx_appointment_customer ON car_wash_appointment(customer_id);
CREATE INDEX idx_appointment_profile ON car_wash_appointment(profile_id);
CREATE INDEX idx_appointment_datetime ON car_wash_appointment(date_time);
CREATE INDEX idx_appointment_status ON car_wash_appointment(status);