CREATE TABLE monthly_platform_stats (
    id UUID PRIMARY KEY,
    year INT NOT NULL,
    month INT NOT NULL,
    gmv_total DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    gmv_marketplace DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    gmv_manual DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    mrr_total DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    mrr_new DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    active_subscriptions INT NOT NULL DEFAULT 0,
    total_users INT NOT NULL DEFAULT 0,
    new_users INT NOT NULL DEFAULT 0,
    total_appointments INT NOT NULL DEFAULT 0,
    completed_appointments INT NOT NULL DEFAULT 0,
    canceled_appointments INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT unique_year_month UNIQUE (year, month)
);

CREATE INDEX idx_monthly_platform_stats_year_month ON monthly_platform_stats(year, month);