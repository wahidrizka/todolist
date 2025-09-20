-- Tabel utama untuk Todo
CREATE TABLE IF NOT EXISTS todos (
                                     id          BIGSERIAL PRIMARY KEY,
                                     title       VARCHAR(255) NOT NULL,
    completed   BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW()
    );

-- Index untuk query status
CREATE INDEX IF NOT EXISTS idx_todos_completed ON todos (completed);

-- Trigger sederhana untuk update updated_at
CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = NOW();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_todos_set_updated_at ON todos;
CREATE TRIGGER trg_todos_set_updated_at
    BEFORE UPDATE ON todos
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();
