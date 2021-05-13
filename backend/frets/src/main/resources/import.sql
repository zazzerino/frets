DROP TABLE IF EXISTS users;

CREATE TABLE users (
       id INT PRIMARY KEY,
       name TEXT,
       session_id TEXT,
       game_id INT
);

-- INSERT INTO users (id, name) VALUES (42, 'Barnabus');
