
ALTER TABLE public.car_wash_appointment
ADD COLUMN IF NOT EXISTS address_id UUID;

ALTER TABLE public.car_wash_appointment
ADD COLUMN IF NOT EXISTS car_type VARCHAR(50);

INSERT INTO public.customer_address (id, customer_id, street, city, state, zip_code, address_name, street_number, created_at, updated_at)
SELECT
    '00000000-0000-0000-0000-000000000000'::UUID,
    (SELECT id FROM public.customer LIMIT 1),
    'Rua Padrão',
    'Cidade Exemplo',
    'Estado Exemplo',
    '00000-000',
    'Endereço Padrão',
    '0',
    NOW(),
    NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM public.customer_address WHERE id = '00000000-0000-0000-0000-000000000000'
);

UPDATE public.car_wash_appointment
SET address_id = COALESCE(address_id, '00000000-0000-0000-0000-000000000000'),
    car_type = COALESCE(car_type, 'DESCONHECIDO')
WHERE address_id IS NULL OR car_type IS NULL;

ALTER TABLE public.car_wash_appointment
ALTER COLUMN address_id SET NOT NULL;

ALTER TABLE public.car_wash_appointment
ALTER COLUMN car_type SET NOT NULL;

ALTER TABLE public.car_wash_appointment
ADD CONSTRAINT fk_car_wash_appointment_address
FOREIGN KEY (address_id)
REFERENCES public.customer_address(id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;

CREATE INDEX IF NOT EXISTS idx_car_wash_appointment_address_id
ON public.car_wash_appointment(address_id);
