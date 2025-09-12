ALTER TABLE advertisements
    ALTER COLUMN title DROP NOT NULL;

ALTER TABLE advertisements
    ALTER COLUMN description DROP NOT NULL;

ALTER TABLE advertisements
    ALTER COLUMN link_url DROP NOT NULL;

ALTER TABLE advertisements
    ALTER COLUMN expires_at DROP NOT NULL;