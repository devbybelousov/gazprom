create table unit
(
    unit_id int auto_increment
        primary key,
    title   varchar(100) not null
);

INSERT INTO gazprom_data.unit (title) VALUES ('Газпром трансгаз Югорск');
INSERT INTO gazprom_data.unit (title) VALUES ('Газпром транссервис');
INSERT INTO gazprom_data.unit (title) VALUES ('Газпром флот');
INSERT INTO gazprom_data.unit (title) VALUES ('Газпром центрремонт');
INSERT INTO gazprom_data.unit (title) VALUES ('Газпром центрэнергогаз');
INSERT INTO gazprom_data.unit (title) VALUES ('Газпром экспо');
INSERT INTO gazprom_data.unit (title) VALUES ('Газпром экспорт');
INSERT INTO gazprom_data.unit (title) VALUES ('Газпром энерго');
INSERT INTO gazprom_data.unit (title) VALUES ('Газпром энергохолдинг');
INSERT INTO gazprom_data.unit (title) VALUES ('Газпромвьет');
INSERT INTO gazprom_data.unit (title) VALUES ('Газпромтранс');