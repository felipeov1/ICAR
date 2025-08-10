ALTER TABLE public.car_wash_registration DROP COLUMN address;ALTER TABLE public.car_wash_registration

ADD COLUMN street VARCHAR(255),
ADD COLUMN "number" VARCHAR(20),
ADD COLUMN complement VARCHAR(100),
ADD COLUMN neighborhood VARCHAR(100),
ADD COLUMN city VARCHAR(100),
ADD COLUMN state VARCHAR(2),
ADD COLUMN zip_code VARCHAR(9);

UPDATE public.car_wash_registration
SET
    street = 'Endereço a ser preenchido',
    "number" = 'S/N',
    neighborhood = 'Bairro a ser preenchido',
    city = 'Ibiporã',
    state = 'PR',
    zip_code = '86200000'
WHERE
    street IS NULL;

ALTER TABLE public.car_wash_registration
ALTER COLUMN street SET NOT NULL,
ALTER COLUMN neighborhood SET NOT NULL,
ALTER COLUMN city SET NOT NULL,
ALTER COLUMN state SET NOT NULL,
ALTER COLUMN zip_code SET NOT NULL;

