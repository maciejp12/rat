DROP TABLE IF EXISTS user_profile CASCADE;
DROP TABLE IF EXISTS offer CASCADE;

CREATE TABLE user_profile (
    user_id Bigserial PRIMARY KEY NOT NULL,
    username varchar(100) UNIQUE NOT NULL,
    email varchar(100) UNIQUE NOT NULL,
    password varchar(100) NOT NULL,
    register_date timestamp NOT NULL
);


CREATE TABLE offer (
    offer_id Bigserial PRIMARY KEY NOT NULL,
    title varchar(100) NOT NULL,
    description varchar(300) NOT NULL,
    price real NOT NULL,
    creator Bigserial REFERENCES user_profile (user_id),
    creation_date timestamp NOT NULL
);


