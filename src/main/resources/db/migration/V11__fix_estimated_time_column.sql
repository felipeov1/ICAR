ALTER TABLE car_wash_offering
ALTER COLUMN estimated_time TYPE INT USING EXTRACT(EPOCH FROM estimated_time) / 60;
