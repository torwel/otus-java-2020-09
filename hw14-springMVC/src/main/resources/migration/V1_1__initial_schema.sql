create table users
(
    id              bigserial not null primary key,
    name            varchar(50),
    login           varchar(50) UNIQUE,
    password        varchar(50),
    administrator   boolean
);
