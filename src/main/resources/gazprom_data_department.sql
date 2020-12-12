create table department
(
    department_id int auto_increment
        primary key,
    title         varchar(100) not null,
    unit_id       int          not null,
    constraint FK9u44ynbcut0e5t0ov7tuokdbk
        foreign key (unit_id) references unit (unit_id)
);

