create table parking_lots
(
    id         bigint not null
        constraint parking_lots_pkey
            primary key,
    lot_number integer,
    status     integer,
    updated_at timestamp
);

alter table parking_lots
    owner to postgres;

INSERT INTO public.parking_lots (id, lot_number, status, updated_at) VALUES (16, 6, 0, '2020-05-06 18:16:29.668000');
INSERT INTO public.parking_lots (id, lot_number, status, updated_at) VALUES (18, 8, 0, '2020-05-06 18:16:29.668000');
INSERT INTO public.parking_lots (id, lot_number, status, updated_at) VALUES (20, 10, 0, '2020-05-06 18:16:29.668000');
INSERT INTO public.parking_lots (id, lot_number, status, updated_at) VALUES (13, 3, 0, '2020-05-06 18:16:29.668000');
INSERT INTO public.parking_lots (id, lot_number, status, updated_at) VALUES (15, 5, 0, '2020-05-06 18:16:29.668000');
INSERT INTO public.parking_lots (id, lot_number, status, updated_at) VALUES (17, 7, 2, '2020-05-12 17:49:36.144000');
INSERT INTO public.parking_lots (id, lot_number, status, updated_at) VALUES (11, 1, 0, '2020-05-12 17:46:48.506000');
INSERT INTO public.parking_lots (id, lot_number, status, updated_at) VALUES (12, 2, 2, '2020-05-12 17:49:29.982000');
INSERT INTO public.parking_lots (id, lot_number, status, updated_at) VALUES (19, 9, 1, '2020-05-06 18:16:29.668000');
INSERT INTO public.parking_lots (id, lot_number, status, updated_at) VALUES (14, 4, 1, '2020-05-06 18:16:29.668000');