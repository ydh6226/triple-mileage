create table if not exists mileage
(
    user_id       binary(16)  not null
        primary key,
    created_date  datetime(6) not null,
    modified_date datetime(6) not null,
    mileage       int         not null
);

create table if not exists mileage_history
(
    id           binary(16)  not null
        primary key,
    created_date datetime(6) not null,
    place_id     binary(16)  not null,
    reason       varchar(30) not null,
    review_id    binary(16)  not null,
    user_id      binary(16)  not null,
    mileage      int         not null
);
