CREATE TABLE blog_posts
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    content    LONGTEXT              NULL,
    created_at datetime              NULL,
    title      VARCHAR(200)          NOT NULL,
    updated_at datetime              NULL,
    user_id    BIGINT                NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE comments
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    content    LONGTEXT              NULL,
    created_at datetime              NULL,
    user_id    BIGINT                NOT NULL,
    post_id    BIGINT                NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE users
(
    id       BIGINT AUTO_INCREMENT NOT NULL,
    email    VARCHAR(100)          NOT NULL,
    password VARCHAR(255)          NOT NULL,
    username VARCHAR(50)           NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT UK6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT UKr43af9ap4edm43mmtq01oddj6 UNIQUE (username);

ALTER TABLE comments
    ADD CONSTRAINT FK82fuwva6wad4s2qk9mpo5i8j2 FOREIGN KEY (post_id) REFERENCES blog_posts (id) ON DELETE NO ACTION;

CREATE INDEX FK82fuwva6wad4s2qk9mpo5i8j2 ON comments (post_id);

ALTER TABLE comments
    ADD CONSTRAINT FK8omq0tc18jd43bu5tjh6jvraq FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE NO ACTION;

CREATE INDEX FK8omq0tc18jd43bu5tjh6jvraq ON comments (user_id);

ALTER TABLE blog_posts
    ADD CONSTRAINT FK9bebj0jhg2mqq7ei9tsy3iwly FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE NO ACTION;

CREATE INDEX FK9bebj0jhg2mqq7ei9tsy3iwly ON blog_posts (user_id);