create table unit
(
    unit_id int auto_increment
        primary key,
    title   varchar(100) not null
);

create table department
(
    department_id int auto_increment
        primary key,
    title         varchar(100) not null,
    unit_id       int          not null
);

create table history
(
    id      int auto_increment
        primary key,
    user_id int          not null,
    reason  varchar(200) null
);

create table users
(
    user_id       int auto_increment
        primary key,
    user_name     varchar(50)  not null,
    password      varchar(100) not null,
    name          varchar(50)  not null,
    last_name     varchar(50)  not null,
    middle_name   varchar(75)  null,
    email         varchar(50)  not null,
    department_id int          null
);

create table information_system
(
    id               int auto_increment
        primary key,
    title            varchar(100) null,
    owner_id         int          not null,
    primary_admin_id int          not null,
    backup_admin_id  int          not null
);

create table roles
(
    role_id int auto_increment
        primary key,
    name    varchar(20) not null
);

create table user_role
(
    user_id int not null,
    role_id int    not null,
    primary key (user_id, role_id)
);

create table privilege
(
    id          int auto_increment
        primary key,
    title       varchar(100) not null,
    description varchar(250) not null
);

create table request
(
    request_id  int auto_increment
        primary key,
    user_id     int         not null,
    system_id   int         not null,
    filing_date datetime    not null,
    expiry_date datetime    not null,
    status      varchar(10) null
);

create table request_history
(
    request_id int not null,
    history_id int not null
);

create table request_user
(
    request_id int not null,
    user_id    int not null
);

create table system_privilege
(
    system_id    int not null,
    privilege_id int null
);
