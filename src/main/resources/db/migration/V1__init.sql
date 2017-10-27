CREATE EXTENSION IF NOT EXISTS citext;

CREATE TABLE person (
  id SERIAL PRIMARY KEY,
  login CITEXT COLLATE "en_US.utf8" NOT NULL UNIQUE,
  password TEXT NOT NULL,
  email CITEXT COLLATE "en_US.utf8" NOT NULL,
  number_of_games INTEGER NOT NULL DEFAULT 0 CHECK (number_of_games >= 0),
  record INTEGER NOT NULL DEFAULT 0 CHECK (record >= 0),
  local_record INTEGER NOT NULL DEFAULT 0 CHECK (record >= 0),
  theme SMALLINT NOT NULL DEFAULT 0,
  created TIMESTAMP NOT NULL DEFAULT now(),
  updated TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_person_login ON person (login);
