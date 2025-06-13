CREATE TABLE car_wash_profile_appointment_config (
    id UUID PRIMARY KEY,
    profile_id UUID NOT NULL UNIQUE,
    min_advance_notice_minutes INTEGER NOT NULL,
    min_edit_notice_minutes INTEGER NOT NULL,
    min_cancel_notice_minutes INTEGER NOT NULL,
    CONSTRAINT fk_appointment_config_profile
        FOREIGN KEY (profile_id) REFERENCES car_wash_profile(id) ON DELETE CASCADE
);