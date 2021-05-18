DROP TABLE IF EXISTS games CASCADE;

CREATE TABLE games (
       id bigint PRIMARY KEY,
       created_at timestamp NOT NULL,
       state text NOT NULL,
       host_id bigint NOT NULL,
       player_ids bigint[]
);

DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
       id bigint PRIMARY KEY NOT NULL,
       name text NOT NULL,
       session_id text UNIQUE NOT NULL,
       game_id bigint
);

DROP TABLE IF EXISTS settings CASCADE;

CREATE TABLE settings (
       round_count integer NOT NULL,
       strings_to_use integer[] NOT NULL,
       accidentals_to_use text[] NOT NULL,
       tuning text[] NOT NULL,
       start_fret integer NOT NULL,
       end_fret integer NOT NULL
);
