CREATE TABLE person (
  id SERIAL PRIMARY KEY,
  login TEXT COLLATE "ucs_basic" NOT NULL UNIQUE,
  password TEXT NOT NULL,
  email TEXT COLLATE "ucs_basic" NOT NULL,
  number_of_games INTEGER NOT NULL DEFAULT 0 CHECK (number_of_games >= 0),
  record INTEGER NOT NULL DEFAULT 0 CHECK (record >= 0),
  local_record INTEGER NOT NULL DEFAULT 0 CHECK (record >= 0),
  theme SMALLINT NOT NULL DEFAULT 0,
  created TIMESTAMP NOT NULL DEFAULT now(),
  updated TIMESTAMP NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX idx_person_email ON person (LOWER(email));
