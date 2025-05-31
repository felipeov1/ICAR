ALTER TABLE car_wash_profile_special_days
DROP COLUMN is_holiday;

ALTER TABLE car_wash_profile_special_days
DROP COLUMN reason;

ALTER TABLE car_wash_profile_special_days
ADD COLUMN reason VARCHAR(50) NOT NULL DEFAULT 'DEFAULT_REASON';