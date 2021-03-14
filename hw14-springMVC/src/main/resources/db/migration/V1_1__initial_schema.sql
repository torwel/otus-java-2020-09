create table addresses
(
    id      bigserial not null primary key,
    street  varchar(100)
);

create table clients
(
    id          bigserial not null primary key,
    name        varchar(50),
    address_id  int8,
    constraint FK_addresses foreign key (address_id) references addresses
);

create table phones
(
    id          bigserial not null primary key,
    number      varchar(20),
    client_id   int8,
    constraint FK_clients foreign key (client_id) references clients
);
