-- Remove unused columns and simplify the special days table
ALTER TABLE car_wash_profile_special_days
DROP COLUMN IF EXISTS description,
DROP COLUMN IF EXISTS reason;

-- Update existing records to use the new simplified logic
-- For closed days (where start_time = '00:00:00' and end_time = '00:00:00'), set to NULL
UPDATE car_wash_profile_special_days
SET start_time = NULL, end_time = NULL
WHERE start_time = '00:00:00' AND end_time = '00:00:00';

-- Add comments to explain the new schema
COMMENT ON TABLE car_wash_profile_special_days IS 'Stores special operating days for car washes. NULL times indicate closed days.';
COMMENT ON COLUMN car_wash_profile_special_days.start_time IS 'Opening time for special day (NULL means closed)';
COMMENT ON COLUMN car_wash_profile_special_days.end_time IS 'Closing time for special day (NULL means closed)';
COMMENT ON COLUMN car_wash_profile_special_days.active IS 'Soft delete flag (false means deleted)';