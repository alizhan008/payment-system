CREATE TABLE public._user
(
    id         bigserial
    primary key,
    email      varchar(255),
    first_name varchar(255),
    last_name  varchar(255),
    password   varchar(255),
    role       varchar(255)
    );

CREATE TABLE public.elcard
(
    id          bigserial
    primary key,
    balance     numeric(38, 2),
    bank_name   varchar(255),
    card_number bigint,
    issue_date  date,
    type        varchar(255),
    user_id     bigint
    constraint fkg3u1uakqm5mvi7liq3rgnty5e
    references public._user
    );

CREATE TABLE public.master_card
(
    id          bigserial
    primary key,
    balance     numeric(38, 2),
    bank_name   varchar(255),
    card_number bigint,
    issue_date  date,
    type        varchar(255),
    user_id     bigint
    constraint fkoligtcf77se6ftmsywdqgxejn
    references public._user
    );

CREATE TABLE public.visa
(
    id          bigserial
        primary key,
    balance     numeric(38, 2),
    bank_name   varchar(255),
    card_number bigint,
    issue_date  date,
    type        varchar(255),
    user_id     bigint
        constraint fkm5wk8ugs6b2qyb8w90tss8l5c
            references public._user
);

CREATE TABLE public.transaction
(
    id            bigserial
        primary key,
    amount_charge numeric(38, 2),
    amount_refill numeric(38, 2),
    bank_name     varchar(255),
    card_number   bigint,
    user_email    varchar(255)
);



