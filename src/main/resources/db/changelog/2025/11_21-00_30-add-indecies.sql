-- =============================================
--  Digital Dicebound – Add Adventure Status
--  File: 11_21-00_30-add-indices.sql
--  Created: 2025‑11‑21 00:30 (UTC+1)
-- =============================================


-- user_profile
CREATE INDEX IF NOT EXISTS idx_user_profile_name
    ON user_profile (name);

CREATE INDEX IF NOT EXISTS idx_user_profile_email
    ON user_profile (email);

-- adventure
CREATE INDEX IF NOT EXISTS idx_adventure_status
    ON adventure (status);

CREATE INDEX IF NOT EXISTS idx_adventure_type
    ON adventure (type);

CREATE INDEX IF NOT EXISTS idx_adventure_title
    ON adventure (title);

CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE INDEX IF NOT EXISTS idx_user_profile_name_trgm
    ON user_profile
        USING gin (lower(name) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS idx_user_profile_email_trgm
    ON user_profile
        USING gin (lower(email) gin_trgm_ops);

-- =============================================
-- End of 11_21-00_30-add-indecies.sql
-- =============================================
