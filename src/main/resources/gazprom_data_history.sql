create table history
(
    id      int auto_increment
        primary key,
    user_id int          not null,
    reason  varchar(200) null
);

