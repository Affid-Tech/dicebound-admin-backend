-- =============================================
--  Digital Dicebound – Initial Schema
--  File: 06_07-21_00-initial-schema.sql
--  Created: 2025‑06‑07 21:00 (UTC+2)
-- =============================================

-- 1️⃣  Core users -------------------------------------------------------------
CREATE TABLE user_profile
(
    id         UUID PRIMARY KEY,
    name       VARCHAR(120) NOT NULL,
    email      VARCHAR(255),
    bio        TEXT,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

-- 2️⃣  Role profiles (Table‑Per‑Type) ----------------------------------------
CREATE TABLE player_profile
(
    user_id UUID PRIMARY KEY REFERENCES user_profile (id) ON DELETE CASCADE
);

CREATE TABLE dungeon_master_profile
(
    user_id UUID PRIMARY KEY REFERENCES user_profile (id) ON DELETE CASCADE
);

CREATE TABLE admin_profile
(
    user_id UUID PRIMARY KEY REFERENCES user_profile (id) ON DELETE CASCADE
);

-- 3️⃣  Adventures & Sessions --------------------------------------------------
CREATE TABLE adventure
(
    id          UUID PRIMARY KEY,
    type        TEXT         NOT NULL,
    game_system TEXT         NOT NULL,
    title       VARCHAR(160) NOT NULL,
    dm_id       UUID         NOT NULL REFERENCES user_profile (id),
    description TEXT,
    start_level SMALLINT,
    min_players SMALLINT     NOT NULL,
    max_players SMALLINT     NOT NULL,
    price_units INT,
    CHECK (min_players <= max_players)
);

CREATE INDEX idx_adventure_dm ON adventure (dm_id);

CREATE TABLE game_session
(
    id             UUID PRIMARY KEY,
    adventure_id   UUID        NOT NULL REFERENCES adventure (id) ON DELETE CASCADE,
    start_time     TIMESTAMPTZ NOT NULL,
    duration_hours SMALLINT    NOT NULL CHECK (duration_hours BETWEEN 1 AND 12),
    link_foundry   TEXT,
    notes          TEXT
);

CREATE INDEX idx_session_start_time ON game_session (start_time);
CREATE INDEX idx_session_adventure ON game_session (adventure_id);

-- 4️⃣  Sign‑ups --------------------------------------------------------------
CREATE TABLE adventure_signup
(
    id           UUID PRIMARY KEY,
    adventure_id UUID                   NOT NULL REFERENCES adventure (id) ON DELETE CASCADE,
    user_id      UUID                   NOT NULL REFERENCES user_profile (id) ON DELETE CASCADE,
    status       TEXT DEFAULT 'PENDING' NOT NULL,
    UNIQUE (adventure_id, user_id)
);

CREATE INDEX idx_signup_user ON adventure_signup (user_id);

-- 5️⃣  Currency rates --------------------------------------------------------
CREATE TABLE currency_rate
(
    currency   CHAR(3) PRIMARY KEY,
    ratio      INT         NOT NULL CHECK (ratio > 0),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- =============================================
-- End of 06_07-21_00-initial-schema.sql
-- =============================================
