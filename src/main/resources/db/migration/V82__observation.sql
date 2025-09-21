ALTER TABLE car_wash_profile
ADD COLUMN wet_wash_observations VARCHAR(500),
ADD COLUMN dry_wash_observations VARCHAR(500);

UPDATE car_wash_profile
SET wet_wash_observations = observations
WHERE observations IS NOT NULL AND observations <> '';