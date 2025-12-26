-- Portfolio Database Schema for PostgreSQL

-- Skills table
CREATE TABLE IF NOT EXISTS skills (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    level INTEGER NOT NULL,
    category VARCHAR(50),
    icon VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Projects table
CREATE TABLE IF NOT EXISTS projects (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    image_url VARCHAR(500),
    github_url VARCHAR(500),
    live_url VARCHAR(500),
    featured BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Project technologies table
CREATE TABLE IF NOT EXISTS project_technologies (
    project_id BIGINT REFERENCES projects(id) ON DELETE CASCADE,
    technology VARCHAR(100)
);

-- Experiences table
CREATE TABLE IF NOT EXISTS experiences (
    id BIGSERIAL PRIMARY KEY,
    job_title VARCHAR(100) NOT NULL,
    company VARCHAR(100) NOT NULL,
    location VARCHAR(100) NOT NULL,
    period VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Experience technologies table
CREATE TABLE IF NOT EXISTS experience_technologies (
    experience_id BIGINT REFERENCES experiences(id) ON DELETE CASCADE,
    technology VARCHAR(100)
);

-- Educations table
CREATE TABLE IF NOT EXISTS educations (
    id BIGSERIAL PRIMARY KEY,
    degree VARCHAR(200) NOT NULL,
    institution VARCHAR(200) NOT NULL,
    location VARCHAR(100) NOT NULL,
    period VARCHAR(100) NOT NULL,
    gpa VARCHAR(10) NOT NULL,
    description TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Education courses table
CREATE TABLE IF NOT EXISTS education_courses (
    education_id BIGINT REFERENCES educations(id) ON DELETE CASCADE,
    course VARCHAR(200)
);

-- Contact messages table
CREATE TABLE IF NOT EXISTS contact_messages (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    subject VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert sample data for skills
INSERT INTO skills (name, level, category, icon) VALUES
('JavaScript', 90, 'Frontend', 'SiJavascript'),
('TypeScript', 85, 'Frontend', 'SiTypescript'),
('React', 90, 'Frontend', 'FaReact'),
('Next.js', 85, 'Frontend', 'SiNextdotjs'),
('Node.js', 85, 'Backend', 'FaNodeJs'),
('Spring Boot', 80, 'Backend', 'SiSpring'),
('Python', 75, 'Backend', 'FaPython'),
('Java', 80, 'Backend', 'FaJava'),
('MySQL', 80, 'Database', 'SiMysql'),
('MongoDB', 75, 'Database', 'SiMongodb'),
('AWS', 70, 'Cloud', 'FaAws'),
('Docker', 75, 'DevOps', 'SiDocker');

-- Insert sample data for projects
INSERT INTO projects (title, description, image_url, github_url, live_url, featured) VALUES
('E-Commerce Platform', 'Full-stack e-commerce solution with React, Node.js, and MongoDB. Features include user authentication, payment processing, and admin dashboard.', '/projects/ecommerce.jpg', 'https://github.com/example/ecommerce', 'https://ecommerce.example.com', TRUE),
('Task Management App', 'Collaborative task management application with real-time updates, drag-and-drop interface, and team collaboration features.', '/projects/taskmanager.jpg', 'https://github.com/example/taskmanager', 'https://taskmanager.example.com', TRUE),
('AI Image Generator', 'Web application that generates images from text prompts using OpenAI''s DALL-E API with user gallery and sharing features.', '/projects/aigenerator.jpg', 'https://github.com/example/aigenerator', 'https://aigenerator.example.com', FALSE),
('Portfolio Website', 'Modern portfolio website with 3D animations, dark mode, and responsive design built with Three.js and Framer Motion.', '/projects/portfolio.jpg', 'https://github.com/example/portfolio', 'https://portfolio.example.com', FALSE),
('Weather Dashboard', 'Real-time weather dashboard with forecasting, map integration, and location-based services using OpenWeatherMap API.', '/projects/weather.jpg', 'https://github.com/example/weather', 'https://weather.example.com', FALSE),
('Social Media Analytics', 'Analytics platform for social media metrics with data visualization, reporting, and competitor analysis features.', '/projects/analytics.jpg', 'https://github.com/example/analytics', 'https://analytics.example.com', FALSE);

-- Insert sample technologies for projects
INSERT INTO project_technologies (project_id, technology) VALUES
(1, 'React'), (1, 'Node.js'), (1, 'MongoDB'), (1, 'Stripe'),
(2, 'Next.js'), (2, 'Firebase'), (2, 'Tailwind CSS'),
(3, 'React'), (3, 'OpenAI API'), (3, 'Node.js'),
(4, 'Next.js'), (4, 'Three.js'), (4, 'Framer Motion'),
(5, 'React'), (5, 'OpenWeatherMap API'), (5, 'Chart.js'),
(6, 'Python'), (6, 'Django'), (6, 'D3.js'), (6, 'PostgreSQL');

-- Insert sample data for experiences
INSERT INTO experiences (job_title, company, location, period, description) VALUES
('Software Engineering Intern', 'TechCorp Inc.', 'San Francisco, CA', 'Jun 2023 - Present', 'Developed scalable web applications using React and Node.js. Collaborated with cross-functional teams to deliver high-quality software solutions. Implemented CI/CD pipelines reducing deployment time by 40%.'),
('Full Stack Developer', 'StartUpXYZ', 'Remote', 'Jan 2022 - May 2023', 'Built and maintained multiple client websites and web applications. Led a team of 3 developers in delivering projects on time. Improved application performance by optimizing database queries and implementing caching strategies.'),
('Research Assistant', 'University of Technology', 'Boston, MA', 'Sep 2021 - Dec 2021', 'Conducted research on machine learning algorithms for natural language processing. Published findings in IEEE conference proceedings. Mentored undergraduate students in programming fundamentals.');

-- Insert sample technologies for experiences
INSERT INTO experience_technologies (experience_id, technology) VALUES
(1, 'React'), (1, 'Node.js'), (1, 'AWS'), (1, 'Docker'),
(2, 'Next.js'), (2, 'MongoDB'), (2, 'Express'), (2, 'Tailwind CSS'),
(3, 'Python'), (3, 'TensorFlow'), (3, 'NLP'), (3, 'Research');

-- Insert sample data for educations
INSERT INTO educations (degree, institution, location, period, gpa, description) VALUES
('Bachelor of Science in Computer Science', 'Stanford University', 'Stanford, CA', '2020 - 2024', '3.8/4.0', 'Specialized in Artificial Intelligence and Machine Learning. Completed capstone project on neural networks for computer vision applications.'),
('Higher Secondary Certificate', 'Greenwood High School', 'San Jose, CA', '2018 - 2020', '4.0/4.0', 'Graduated with honors. Participated in mathematics and computer science olympiads.');

-- Insert sample courses for educations
INSERT INTO education_courses (education_id, course) VALUES
(1, 'Data Structures & Algorithms'), (1, 'Machine Learning'), (1, 'Computer Networks'), (1, 'Database Systems'),
(2, 'Calculus'), (2, 'Physics'), (2, 'Computer Science Principles'), (2, 'Statistics');
