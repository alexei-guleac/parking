create table if not exists confirmation_tokens
(
    token_id           bigint       not null,
    claimed            boolean,
    confirmation_token varchar(255),
    created_at         timestamp,
    expiration_date    timestamp,
    operation_type     integer,
    uid                varchar(255) not null,
    constraint confirmation_tokens_pkey
        primary key (token_id)
);

alter table confirmation_tokens
    owner to postgres;

create table if not exists parking_lots
(
    id         bigint not null,
    lot_number integer,
    status     integer,
    updated_at timestamp,
    constraint parking_lots_pkey
        primary key (id)
);

alter table parking_lots
    owner to postgres;

create table if not exists statistics
(
    id                 bigserial not null,
    lot_number         integer,
    parking_lot_status integer,
    updated_at         timestamp,
    constraint statistics_pkey
        primary key (id)
);

alter table statistics
    owner to postgres;


