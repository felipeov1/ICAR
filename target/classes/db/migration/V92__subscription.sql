ALTER TABLE push_subscriptions
ADD COLUMN car_wash_profile_id UUID;

ALTER TABLE push_subscriptions
ADD CONSTRAINT fk_push_subscriptions_on_car_wash_profile
FOREIGN KEY (car_wash_profile_id) REFERENCES car_wash_profile(id);



