-- =============================================
--  Digital Dicebound – Add Adventure Status
--  File: 06_18-22_30-adventure-status.sql
--  Created: 2025‑06‑18 22:30 (UTC+2)
-- =============================================


-- 3️⃣  Adventures & Sessions --------------------------------------------------
ALTER TABLE adventure
ADD COLUMN IF NOT EXISTS status TEXT NOT NULL DEFAULT 'COMPLETED';
-- =============================================
-- End of 06_18-22_30-adventure-status.sql
-- =============================================
