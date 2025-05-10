ALTER TABLE car_wash
ADD COLUMN cnpj VARCHAR(18),
ADD COLUMN cpf VARCHAR(11),
ADD COLUMN owner_name VARCHAR(255);

UPDATE car_wash
SET cnpj = CASE
              WHEN LENGTH(cnpj_cpf) = 18 THEN cnpj_cpf
              ELSE NULL
            END,
    cpf = CASE
             WHEN LENGTH(cnpj_cpf) = 11 THEN cnpj_cpf
             ELSE NULL
           END;

ALTER TABLE car_wash
DROP COLUMN cnpj_cpf;

ALTER TABLE car_wash
ADD CONSTRAINT cnpj_or_cpf_check CHECK (
    (cnpj IS NOT NULL AND cpf IS NULL) OR (cnpj IS NULL AND cpf IS NOT NULL)
);
