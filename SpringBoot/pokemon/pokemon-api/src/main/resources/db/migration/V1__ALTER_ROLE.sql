CREATE TABLE pokemon
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    `description` VARCHAR(255)          NULL,
    hp            INT                   NULL,
    name          VARCHAR(255)          NULL,
    type          VARCHAR(255)          NULL,
    image_url     VARCHAR(255)          NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE users
(
    id       BIGINT AUTO_INCREMENT NOT NULL,
    password VARCHAR(255)          NOT NULL,
    username VARCHAR(255)          NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT UKr43af9ap4edm43mmtq01oddj6 UNIQUE (username);