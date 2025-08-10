DROP TABLE IF EXISTS car_wash_profile_special_days;

CREATE TABLE car_wash_profile_special_days (
    id UUID PRIMARY KEY,
    profile_id UUID NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    description VARCHAR(255),
    is_closed BOOLEAN NOT NULL DEFAULT TRUE,
    start_time TIME,
    end_time TIME,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE,

    CONSTRAINT fk_special_days_profile
        FOREIGN KEY (profile_id)
        REFERENCES car_wash_profile(id)
        ON DELETE CASCADE,

    CONSTRAINT chk_date_range CHECK (end_date >= start_date),

    CONSTRAINT chk_times_if_not_closed
        CHECK (is_closed OR (start_time IS NOT NULL AND end_time IS NOT NULL))
);

CREATE INDEX idx_special_days_profile_id_dates_active
ON car_wash_profile_special_days (profile_id, end_date, deleted_at);