ALTER TABLE public.car_wash_appointment
ADD COLUMN company_customer_id UUID,
ADD CONSTRAINT fk_appointment_company_customer
   FOREIGN KEY (company_customer_id)
   REFERENCES public.company_customer(id);