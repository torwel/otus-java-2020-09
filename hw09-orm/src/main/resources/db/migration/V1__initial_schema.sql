create table client
(
    id      bigserial not null primary key,
    name    varchar(50),
    age     integer
);
create table account
(
    no      varchar(16),
    type    varchar(20),
    rest    double precision
);