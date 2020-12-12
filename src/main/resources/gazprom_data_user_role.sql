create table user_role
(
    user_id bigint not null,
    role_id int    not null,
    primary key (user_id, role_id)
)
    engine = MyISAM;

create index FKt7e7djp752sqn6w22i6ocqy6q
    on user_role (role_id);

