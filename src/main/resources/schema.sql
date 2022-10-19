DROP TABLE people IF EXISTS;

CREATE TABLE people (
    person_id BIGINT auto_increment NOT NULL PRIMARY KEY,
    name VARCHAR(20),
    surname VARCHAR(20)
);
