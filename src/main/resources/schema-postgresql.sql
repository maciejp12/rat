DROP TABLE IF EXISTS user_profile CASCADE;
DROP TABLE IF EXISTS offer CASCADE;
DROP TABLE IF EXISTS offer_visit CASCADE;
DROP TABLE IF EXISTS offer_image CASCADE;


CREATE TABLE user_profile (
    user_id BIGSERIAL PRIMARY KEY NOT NULL,
    username VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    phone_number VARCHAR(9),
    register_date TIMESTAMP NOT NULL
);


CREATE TABLE offer (
    offer_id BIGSERIAL PRIMARY KEY NOT NULL,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(300) NOT NULL,
    price NUMERIC(16, 2) NOT NULL,
    creator BIGINT REFERENCES user_profile (user_id),
    creation_date TIMESTAMP NOT NULL
);


CREATE TABLE offer_visit (
    visit_id BIGSERIAL PRIMARY KEY NOT NULL,
    visited_offer BIGINT REFERENCES offer (offer_id),
    visitor BIGINT REFERENCES user_profile (user_id),
    visit_date TIMESTAMP NOT NULL
);

CREATE TABLE offer_image (
    offer_image_id BIGSERIAL PRIMARY KEY NOT NULL,
    image_file_name VARCHAR(100) NOT NULL,
    offer_id BIGINT REFERENCES offer (offer_id),
    image_type VARCHAR(16)
);
