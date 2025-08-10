CREATE INDEX IF NOT EXISTS idx_appointment_profile_status_time ON car_wash_appointment (profile_id, status, date_time);

CREATE INDEX IF NOT EXISTS idx_review_profile_time ON reviews (profile_id, created_at DESC);