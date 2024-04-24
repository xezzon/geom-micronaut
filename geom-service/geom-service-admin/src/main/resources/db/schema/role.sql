--liquibase formatted sql

--changeset xezzon:1 dbms:postgresql,mysql,mariadb
CREATE TABLE geom_role (
  id VARCHAR(64) NOT NULL,
   code VARCHAR(255) NOT NULL,
   name VARCHAR(255) NOT NULL,
   inheritable BOOLEAN NOT NULL,
   parent_id VARCHAR(255) NOT NULL,
   CONSTRAINT pk_geom_role PRIMARY KEY (id)
);
ALTER TABLE geom_role ADD CONSTRAINT uc_geom_role_code UNIQUE (code);
