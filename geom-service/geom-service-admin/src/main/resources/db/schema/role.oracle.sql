--liquibase formatted sql

--changeset xezzon:1 dbms:oracle
CREATE TABLE geom_role (
  id VARCHAR2(64) NOT NULL,
   code VARCHAR2(255) NOT NULL,
   name VARCHAR2(255) NOT NULL,
   inheritable NUMBER(1) NOT NULL,
   parentId VARCHAR2(255) NOT NULL,
   CONSTRAINT pk_geom_role PRIMARY KEY (id)
);
ALTER TABLE geom_role ADD CONSTRAINT uc_geom_role_code UNIQUE (code);
