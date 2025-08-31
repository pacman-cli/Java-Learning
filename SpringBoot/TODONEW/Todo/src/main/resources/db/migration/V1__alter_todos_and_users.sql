CREATE TABLE todos
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    completed     BIT(1)       NOT NULL,
    created_at    datetime NULL,
    `description` VARCHAR(1000) NULL,
    due_date      date NULL,
    title         VARCHAR(255) NOT NULL,
    updated_at    datetime NULL,
    user_id       BIGINT       NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE users
(
    id       BIGINT AUTO_INCREMENT NOT NULL,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT UKr43af9ap4edm43mmtq01oddj6 UNIQUE (username);

ALTER TABLE todos
    ADD CONSTRAINT FK9605g76a1dggbvs18f2r80gvu FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE NO ACTION;

