DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
       id integer PRIMARY KEY NOT NULL,
       name text NOT NULL,
       session_id text UNIQUE NOT NULL
);

DROP TABLE IF EXISTS games CASCADE;

DROP TYPE IF EXISTS game_state;

CREATE TYPE game_state AS ENUM ('INIT', 'PLAYING', 'ROUND_OVER', 'GAME_OVER');

CREATE TABLE games (
       id integer PRIMARY KEY,
       created_at timestamp NOT NULL,
       state text NOT NULL,
       round_count integer NOT NULL
);

DROP TABLE IF EXISTS game_users;

CREATE TABLE game_users (
       game_id integer REFERENCES games(id),
       user_id integer REFERENCES users(id)
);
