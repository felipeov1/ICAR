ALTER TABLE customer ADD COLUMN auth_provider VARCHAR(255);

UPDATE customer SET auth_provider = 'LOCAL';

ALTER TABLE customer ALTER COLUMN auth_provider SET NOT NULL;