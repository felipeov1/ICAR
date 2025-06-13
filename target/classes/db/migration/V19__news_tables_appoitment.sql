CREATE TABLE IF NOT EXISTS car_wash_weekly_schedule (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    profile_id UUID NOT NULL,
    day_of_week VARCHAR(20) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    available BOOLEAN NOT NULL,
    appointment_interval_minutes INTEGER NOT NULL,
    FOREIGN KEY (profile_id) REFERENCES car_wash_profile(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS car_wash_special_days (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    profile_id UUID NOT NULL,
    date DATE NOT NULL,
    start_time TIME,
    end_time TIME,
    is_holiday BOOLEAN NOT NULL,
    reason VARCHAR(255) NOT NULL,
    FOREIGN KEY (profile_id) REFERENCES car_wash_profile(id) ON DELETE CASCADE
);

CREATE INDEX idx_weekly_schedule_profile ON car_wash_weekly_schedule(profile_id);
CREATE INDEX idx_weekly_schedule_day ON car_wash_weekly_schedule(day_of_week);
CREATE INDEX idx_special_days_profile ON car_wash_special_days(profile_id);
CREATE INDEX idx_special_days_date ON car_wash_special_days(date);

DROP TABLE IF EXISTS car_wash_profile_opening_hours;

ALTER TABLE car_wash_profile ADD COLUMN IF NOT EXISTS active BOOLEAN DEFAULT TRUE;

ALTER TABLE car_wash_offering
    ALTER COLUMN price TYPE NUMERIC(10,2),
    ALTER COLUMN estimated_time SET NOT NULL,
    ALTER COLUMN modality SET NOT NULL;