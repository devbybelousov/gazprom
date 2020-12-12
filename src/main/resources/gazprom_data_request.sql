create table request
(
    request_id  int auto_increment
        primary key,
    user_id     int         not null,
    system_id   int         not null,
    filing_date datetime    not null,
    expiry_date datetime    not null,
    status      varchar(10) null,
    constraint FK7vrq809dxla5762q0jw6qxlmx
        foreign key (system_id) references information_system (id),
    constraint FKg03bldv86pfuboqfefx48p6u3
        foreign key (user_id) references users (user_id)
);

