create table roles
(
    role_id int auto_increment
        primary key,
    name    varchar(20) not null
);

INSERT INTO gazprom_data.roles (name) VALUES ('ROLE_USER');