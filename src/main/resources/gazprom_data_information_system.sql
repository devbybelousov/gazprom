create table information_system
(
    id               int auto_increment
        primary key,
    title            varchar(100) null,
    owner_id         int          not null,
    primary_admin_id int          not null,
    backup_admin_id  int          not null
);

INSERT INTO gazprom_data.information_system (title, owner_id, primary_admin_id, backup_admin_id) VALUES ('Информационная система 1', 0, 0, 0);
INSERT INTO gazprom_data.information_system (title, owner_id, primary_admin_id, backup_admin_id) VALUES ('Информационная система 2', 0, 0, 0);
INSERT INTO gazprom_data.information_system (title, owner_id, primary_admin_id, backup_admin_id) VALUES ('Информационная система 3', 0, 0, 0);
INSERT INTO gazprom_data.information_system (title, owner_id, primary_admin_id, backup_admin_id) VALUES ('Информационная система 4', 0, 0, 0);
INSERT INTO gazprom_data.information_system (title, owner_id, primary_admin_id, backup_admin_id) VALUES ('Информационная система 5', 0, 0, 0);
INSERT INTO gazprom_data.information_system (title, owner_id, primary_admin_id, backup_admin_id) VALUES ('Информационная система 6', 0, 0, 0);
INSERT INTO gazprom_data.information_system (title, owner_id, primary_admin_id, backup_admin_id) VALUES ('Информационная система 7', 0, 0, 0);
INSERT INTO gazprom_data.information_system (title, owner_id, primary_admin_id, backup_admin_id) VALUES ('Информационная система 8', 0, 0, 0);
INSERT INTO gazprom_data.information_system (title, owner_id, primary_admin_id, backup_admin_id) VALUES ('Информационная система 9', 0, 0, 0);
INSERT INTO gazprom_data.information_system (title, owner_id, primary_admin_id, backup_admin_id) VALUES ('Информационная система 10', 0, 0, 0);