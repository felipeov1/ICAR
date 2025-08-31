ALTER TABLE car_wash_registration ADD COLUMN trade_name VARCHAR(255);

UPDATE car_wash_registration SET trade_name = legal_name;

ALTER TABLE car_wash_registration ALTER COLUMN trade_name SET NOT NULL;

ALTER TABLE car_wash_registration DROP COLUMN legal_name;
ALTER TABLE car_wash_registration DROP COLUMN street;
ALTER TABLE car_wash_registration DROP COLUMN number;
ALTER TABLE car_wash_registration DROP COLUMN neighborhood;