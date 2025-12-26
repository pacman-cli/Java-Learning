-- Student Results Database Schema
CREATE TABLE IF NOT EXISTS students
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100)        NOT NULL,
    last_name  VARCHAR(100)        NOT NULL,
    email      VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS subjects
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(100)       NOT NULL,
    code       VARCHAR(20) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS exam_results
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id     BIGINT NOT NULL,
    subject_id     BIGINT NOT NULL,
    marks_obtained INT    NOT NULL CHECK (marks_obtained >= 0 AND marks_obtained <= 100),
    total_marks    INT    NOT NULL CHECK (total_marks > 0),
    exam_date      DATE   NOT NULL,
    status         ENUM ('PASSED', 'FAILED', 'PENDING') DEFAULT 'PENDING',
    created_at     TIMESTAMP                            DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects (id) ON DELETE CASCADE,

    INDEX idx_exam_date (exam_date),
    INDEX idx_student_subject (student_id, subject_id)
);