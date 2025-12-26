CREATE TABLE places
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    address   VARCHAR(255) NULL,
    latitude  VARCHAR(255) NOT NULL,
    longitude VARCHAR(255) NOT NULL,
    name      VARCHAR(255) NOT NULL,
    type      VARCHAR(255) NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);