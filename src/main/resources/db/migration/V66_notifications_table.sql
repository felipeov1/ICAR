CREATE TABLE notifications (
    id UUID PRIMARY KEY,
    profile_id UUID NOT NULL,
    type VARCHAR(50) NOT NULL,
    text VARCHAR(255) NOT NULL,
    appointment_time VARCHAR(100),
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT fk_notifications_profile FOREIGN KEY (profile_id) REFERENCES car_wash_profile(id)
);

CREATE INDEX idx_notifications_profile_id ON notifications (profile_id);