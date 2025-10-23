ALTER TABLE push_subscriptions
ADD COLUMN subscription_type VARCHAR(50);

UPDATE push_subscriptions
SET subscription_type = 'CARWASH'
WHERE subscription_type IS NULL;

ALTER TABLE push_subscriptions
ALTER COLUMN subscription_type SET NOT NULL;
