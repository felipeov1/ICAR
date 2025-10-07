CREATE TABLE push_subscriptions (
    id UUID PRIMARY KEY,
    profile_id UUID NOT NULL,
    endpoint VARCHAR(512) NOT NULL UNIQUE,
    p256dh VARCHAR(255) NOT NULL,
    auth VARCHAR(255) NOT NULL,
    CONSTRAINT fk_push_subscriptions_profile
        FOREIGN KEY(profile_id)
        REFERENCES car_wash_profile(id)
        ON DELETE CASCADE
);