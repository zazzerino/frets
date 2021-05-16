DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
       id integer PRIMARY KEY NOT NULL,
       name text NOT NULL,
       session_id text UNIQUE NOT NULL
);

DROP TABLE IF EXISTS games CASCADE;

CREATE TABLE games (
       id integer PRIMARY KEY,
       created_at timestamp NOT NULL,
       state text NOT NULL,
       round_count integer NOT NULL,
       strings_to_use integer[] NOT NULL,
       accidentals_to_use text[] NOT NULL,
       host_id integer NOT NULL,
       player_ids integer[]
);
