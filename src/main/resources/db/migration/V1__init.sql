CREATE TABLE  postgres._user
(
    id         bigserial
    primary key,
    email      varchar(255),
    first_name varchar(255),
    last_name  varchar(255),
    password   varchar(255),
    role       varchar(255)
    );
