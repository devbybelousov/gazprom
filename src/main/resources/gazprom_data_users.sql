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
    department_id int          null,
    constraint FKfi832e3qv89fq376fuh8920y4
        foreign key (department_id) references department (department_id)
);

