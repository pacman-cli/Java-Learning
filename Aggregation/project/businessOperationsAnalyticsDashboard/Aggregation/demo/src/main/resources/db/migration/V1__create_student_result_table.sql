CREATE TABLE student_result
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    student_name VARCHAR(255) NULL,
    subject      VARCHAR(255) NULL,
    marks        INT NULL,
    CONSTRAINT pk_student_result PRIMARY KEY (id)
);