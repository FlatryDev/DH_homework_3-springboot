CREATE TABLE department
(
    id     INTEGER NOT NULL CONSTRAINT department_pkey PRIMARY KEY,
    name   VARCHAR(80) NOT NULL CONSTRAINT idx_department_name UNIQUE,
    closed BOOLEAN DEFAULT FALSE NOT NULL
);

CREATE TABLE person
(
    id          INTEGER NOT NULL CONSTRAINT person_pkey PRIMARY KEY,
    first_name  VARCHAR(80) NOT NULL,
    last_name   VARCHAR(80) NOT NULL,
    middle_name VARCHAR(80),
    age         INTEGER,
    id_department integer
);

ALTER TABLE person
ADD CONSTRAINT person_fk_department
FOREIGN KEY (id_department) REFERENCES department(id)
ON DELETE set null
ON UPDATE CASCADE;


CREATE SEQUENCE sequence_person
  INCREMENT BY 1
  MINVALUE 1
  START 1
  CACHE 1
  NO CYCLE;

CREATE SEQUENCE sequence_department
  INCREMENT BY 1
  MINVALUE 1
  START 1
  CACHE 1
  NO CYCLE;