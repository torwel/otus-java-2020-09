create table client
(
    id      bigserial not null primary key,
    name    varchar(50),
    age     integer
);

create sequence account_no_seq
start with 10000;

create table account
(
    no      varchar(7) not null primary key default nextval('account_no_seq')::text,
    type    varchar(20),
    rest    double precision
);