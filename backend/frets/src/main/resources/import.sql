DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
       id integer PRIMARY KEY NOT NULL,
       name text NOT NULL,
       session_id text UNIQUE NOT NULL
);

DROP TYPE IF EXISTS game_state;

CREATE TYPE game_state AS ENUM ('INIT', 'PLAYING', 'ROUND_OVER', 'GAME_OVER');

DROP TABLE IF EXISTS games CASCADE;

CREATE TABLE games (
       id integer PRIMARY KEY,
       created_at timestamp NOT NULL,
       state text NOT NULL,
       round_count integer NOT NULL,
       strings_to_use integer[] NOT NULL
);

DROP TABLE IF EXISTS game_users;

CREATE TABLE game_users (
       game_id integer REFERENCES games(id),
       user_id integer REFERENCES users(id)
);

-- INSERT INTO users (id, name, session_id) values (-1, 'Alice', 'lakshfd');
-- INSERT INTO games (id, created_at, player_ids, state, round_count) values (-1, '2001-01-01 03:00:00', '{-1}', 'INIT', 4);
