-- Insert sample data for demonstration
INSERT INTO students (first_name, last_name, email)
VALUES ('John', 'Doe', 'john.doe@example.com'),
       ('Jane', 'Smith', 'jane.smith@example.com'),
       ('Bob', 'Johnson', 'bob.j@example.com'),
       ('Alice', 'Williams', 'alice.w@example.com');

INSERT INTO subjects (name, code)
VALUES ('Mathematics', 'MATH101'),
       ('Science', 'SCI201'),
       ('History', 'HIST301'),
       ('English', 'ENG401');

-- Insert exam results
INSERT INTO exam_results (student_id, subject_id, marks_obtained, total_marks, exam_date, status)
VALUES (1, 1, 85, 100, '2023-01-15', 'PASSED'),
       (1, 2, 72, 100, '2023-01-15', 'PASSED'),
       (2, 1, 65, 100, '2023-01-15', 'PASSED'),
       (2, 3, 58, 100, '2023-01-15', 'FAILED'),
       (3, 2, 45, 100, '2023-01-15', 'FAILED'),
       (3, 4, 78, 100, '2023-01-15', 'PASSED'),
       (4, 1, 92, 100, '2023-01-15', 'PASSED'),
       (4, 3, 88, 100, '2023-01-15', 'PASSED'),

       (1, 1, 78, 100, '2023-02-20', 'PASSED'),
       (1, 2, 81, 100, '2023-02-20', 'PASSED'),
       (2, 1, 70, 100, '2023-02-20', 'PASSED'),
       (2, 3, 62, 100, '2023-02-20', 'PASSED'),
       (3, 2, 55, 100, '2023-02-20', 'FAILED'),
       (3, 4, 82, 100, '2023-02-20', 'PASSED'),
       (4, 1, 95, 100, '2023-02-20', 'PASSED'),
       (4, 3, 90, 100, '2023-02-20', 'PASSED'),

       (1, 1, 82, 100, '2023-03-10', 'PASSED'),
       (1, 2, 75, 100, '2023-03-10', 'PASSED'),
       (2, 1, 68, 100, '2023-03-10', 'PASSED'),
       (2, 3, 65, 100, '2023-03-10', 'PASSED'),
       (3, 2, 60, 100, '2023-03-10', 'PASSED'),
       (3, 4, 85, 100, '2023-03-10', 'PASSED'),
       (4, 1, 97, 100, '2023-03-10', 'PASSED'),
       (4, 3, 92, 100, '2023-03-10', 'PASSED');