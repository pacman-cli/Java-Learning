CREATE TABLE orders
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    user_id      BIGINT       NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    quantity     INT          NOT NULL,
    price DOUBLE NOT NULL,
    status       VARCHAR(255) NOT NULL,
    created_at   datetime NULL,
    CONSTRAINT pk_orders PRIMARY KEY (id)
);