-- V1__Initial_schema.sql
CREATE TYPE appointment_status AS ENUM ('pending', 'completed', 'canceled');
CREATE TYPE service_modality AS ENUM ('in_person', 'at_home');
CREATE TYPE payment_method_type AS ENUM ('credit_card', 'pix');

-- Car Wash
CREATE TABLE car_wash (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    cnpj_cpf VARCHAR(18) NOT NULL,
    legal_name VARCHAR(255) NOT NULL,
    trade_name VARCHAR(255),
    phone VARCHAR(15) NOT NULL,
    email VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- Car Wash Profile
CREATE TABLE car_wash_profile (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    car_wash_id UUID REFERENCES car_wash(id),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    address VARCHAR(255) NOT NULL,
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    cover_photo VARCHAR(255),
    photos JSONB,
    opening_hours JSONB,
    vehicle_types JSONB
);

-- Services
CREATE TABLE service (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    car_wash_id UUID REFERENCES car_wash(id),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    modality service_modality,
    price DECIMAL(10, 2) NOT NULL,
    estimated_time INTERVAL,
    materials_needed TEXT
);

-- Customers
CREATE TABLE customer (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    password VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- Appointments
CREATE TABLE appointment (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    customer_id UUID REFERENCES customer(id),
    car_wash_id UUID REFERENCES car_wash(id),
    service_id UUID REFERENCES service(id),
    date_time TIMESTAMP NOT NULL,
    status appointment_status DEFAULT 'pending',
    payment_method payment_method_type,
    amount_paid DECIMAL(10, 2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- Service Completion
CREATE TABLE service_completion (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    appointment_id UUID REFERENCES appointment(id),
    status appointment_status NOT NULL,
    completion_date_time TIMESTAMP
);

-- Reviews
CREATE TABLE review (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    customer_id UUID REFERENCES customer(id),
    car_wash_id UUID REFERENCES car_wash(id),
    service_id UUID REFERENCES service(id),
    rating INT CHECK (rating BETWEEN 1 AND 5),
    feedback TEXT
);

-- Payments
CREATE TABLE payment (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    appointment_id UUID REFERENCES appointment(id),
    payment_intent_id VARCHAR(255) NOT NULL,
    payment_method payment_method_type,
    payment_status VARCHAR(50) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    currency VARCHAR(10) DEFAULT 'BRL',
    stripe_fee DECIMAL(10, 2),
    pix_code TEXT,
    pix_expiration TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Car Wash Closures
CREATE TABLE car_wash_closure (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    car_wash_id UUID REFERENCES car_wash(id),
    closure_date DATE NOT NULL,
    reason VARCHAR(255),
    UNIQUE (car_wash_id, closure_date)
);

-- Notifications
CREATE TABLE notification (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id UUID,
    user_type VARCHAR(50),
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Promotions
CREATE TABLE promotion (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    car_wash_id UUID REFERENCES car_wash(id),
    service_id UUID REFERENCES service(id),
    discount DECIMAL(5, 2) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    description TEXT
);

-- index for improve the performance
CREATE INDEX idx_appointment_customer_id ON appointment(customer_id);
CREATE INDEX idx_appointment_car_wash_id ON appointment(car_wash_id);
CREATE INDEX idx_appointment_date_time ON appointment(date_time);
CREATE INDEX idx_service_car_wash_id ON service(car_wash_id);
CREATE INDEX idx_review_car_wash_id ON review(car_wash_id);
CREATE INDEX idx_payment_appointment_id ON payment(appointment_id);
CREATE INDEX idx_notification_user_id ON notification(user_id);