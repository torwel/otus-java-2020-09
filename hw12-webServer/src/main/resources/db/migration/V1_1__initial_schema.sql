create table clients
(
    id      bigserial not null primary key,
    name    varchar(50)
);

create table addresses
(
    id      bigserial not null primary key,
    street  varchar(100)
);

create table phones
(
    id      bigserial not null primary key,
    number  varchar(20)
);

create table users
(
    id              bigserial not null primary key,
    name            varchar(50),
    login           varchar(50) UNIQUE,
    password        varchar(50),
    administrator   boolean
);
