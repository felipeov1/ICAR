UPDATE push_subscriptions
SET car_wash_profile_id = profile_id
WHERE car_wash_profile_id IS NULL;

ALTER TABLE push_subscriptions
DROP COLUMN profile_id;

ALTER TABLE push_subscriptions
ALTER COLUMN car_wash_profile_id DROP NOT NULL;

ALTER TABLE push_subscriptions
ADD COLUMN IF NOT EXISTS subscription_type VARCHAR(50);

UPDATE push_subscriptions
SET subscription_type = 'CARWASH'
WHERE subscription_type IS NULL;

ALTER TABLE push_subscriptions
ALTER COLUMN subscription_type SET NOT NULL;
