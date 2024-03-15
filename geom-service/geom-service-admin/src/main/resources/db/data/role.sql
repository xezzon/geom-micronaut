--liquibase formatted sql

--changeset xezzon:1 dbms:postgresql,mysql,mariadb
INSERT INTO geom_role (id, code, name, inheritable, parent_id) VALUES
('1', 'root', '超级管理员', true, '0'),
('2', 'superuser', '站点管理员', false, '1'),
('3', 'admin', '系统管理员', true , '1');
