# Professional Portfolio Website

A cutting-edge, professional portfolio website for a CSE student built with modern technologies. The project consists of a Next.js frontend, Spring Boot backend, and MySQL database.

![Portfolio Preview](portfolio-preview.png)

## Features

### Frontend (Next.js)

- 🌗 Dark/Light mode toggle
- 🎨 Modern UI with gradient accents and smooth animations
- 📱 Fully responsive design
- 🚀 Optimized performance with Next.js
- 🎭 Interactive components with Framer Motion
- 📊 Animated skill bars
- 🗂️ Filterable project showcase
- 📅 Experience timeline
- 📞 Contact form with validation
- 🔧 Mobile-friendly navigation

### Backend (Spring Boot)

- 🚀 RESTful API endpoints
- 🗄️ MySQL database integration
- 🔄 CRUD operations for portfolio data
- 🛡️ Data validation
- 🌐 CORS enabled for frontend integration
- 📦 Modular architecture with services, repositories, and controllers

### Database (MySQL)

- 🗃️ Complete portfolio data schema
- 📚 Sample data for demonstration
- 🔄 Relationship mapping between entities

## Tech Stack

### Frontend

- **Next.js 14** - React framework for production
- **Tailwind CSS** - Utility-first CSS framework
- **Framer Motion** - Animation library for React
- **AOS (Animate On Scroll)** - Scroll animations
- **React Icons** - Popular icon library

### Backend

- **Spring Boot 3.2** - Java framework for building web applications
- **Spring Data JPA** - ORM for database operations
- **MySQL** - Relational database
- **Maven** - Dependency management
- **Hibernate** - Object-relational mapping
- **Jakarta Validation** - Data validation

### Database

- **MySQL 8.0** - Relational database management system

## Project Structure

```
portfolio/
├── portfolio-frontend/         # Next.js frontend
│   ├── public/                 # Static assets
│   ├── src/
│   │   ├── app/                # Next.js app directory
│   │   ├── components/         # Reusable components
│   │   ├── styles/             # Global styles
│   │   └── utils/              # Utility functions
│   ├── tailwind.config.js      # Tailwind CSS configuration
│   └── package.json            # Frontend dependencies
├── portfolio-backend/          # Spring Boot backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/portfolio/
│   │   │   │   ├── controller/ # REST controllers
│   │   │   │   ├── dto/        # Data transfer objects
│   │   │   │   ├── entity/     # JPA entities
│   │   │   │   ├── repository/ # Spring Data repositories
│   │   │   │   └── service/    # Business logic services
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   ├── pom.xml                 # Maven configuration
│   └── README.md               # Backend documentation
├── database/                   # Database schema and scripts
│   └── portfolio_schema.sql    # MySQL schema
├── docker-compose.yml          # Docker configuration
└── README.md                   # Project documentation
```

## Getting Started

### Prerequisites

- Node.js (v16 or higher)
- Java 17 or higher
- Maven 3.6 or higher
- Docker (optional, for easy MySQL setup)
- MySQL 8.0 or higher (if not using Docker)

### Quick Setup with Docker

1. Start the MySQL database:

```bash
docker-compose up -d
```

2. Wait for the database to initialize (check logs with `docker-compose logs -f mysql`)

### Manual Setup

1. Create a MySQL database:

```sql
CREATE DATABASE portfolio_db;
```

2. Update database credentials in `portfolio-backend/src/main/resources/application.properties`

### Frontend Setup

1. Navigate to the frontend directory:

```bash
cd portfolio-frontend
```

2. Install dependencies:

```bash
npm install
```

3. Run the development server:

```bash
npm run dev
```

The frontend will start on [http://localhost:3000](http://localhost:3000)

### Backend Setup

1. Navigate to the backend directory:

```bash
cd portfolio-backend
```

2. Build the project:

```bash
mvn clean install
```

3. Run the application:

```bash
mvn spring-boot:run
```

The backend will start on [http://localhost:8080](http://localhost:8080)

## API Endpoints

### Portfolio Data

- `GET /api/portfolio/data` - Get all portfolio data in a single response

### Skills

- `GET /api/skills` - Get all skills
- `GET /api/skills/{id}` - Get skill by ID
- `POST /api/skills` - Create a new skill
- `PUT /api/skills/{id}` - Update a skill
- `DELETE /api/skills/{id}` - Delete a skill

### Projects

- `GET /api/projects` - Get all projects
- `GET /api/projects/featured` - Get featured projects
- `GET /api/projects/{id}` - Get project by ID
- `POST /api/projects` - Create a new project
- `PUT /api/projects/{id}` - Update a project
- `DELETE /api/projects/{id}` - Delete a project

### Experiences

- `GET /api/experiences` - Get all experiences
- `GET /api/experiences/{id}` - Get experience by ID
- `POST /api/experiences` - Create a new experience
- `PUT /api/experiences/{id}` - Update an experience
- `DELETE /api/experiences/{id}` - Delete an experience

### Educations

- `GET /api/educations` - Get all educations
- `GET /api/educations/{id}` - Get education by ID
- `POST /api/educations` - Create a new education
- `PUT /api/educations/{id}` - Update an education
- `DELETE /api/educations/{id}` - Delete an education

### Contact Messages

- `GET /api/contact-messages` - Get all messages
- `GET /api/contact-messages/{id}` - Get message by ID
- `POST /api/contact-messages` - Create a new message
- `PUT /api/contact-messages/{id}` - Update a message
- `DELETE /api/contact-messages/{id}` - Delete a message

## Customization

### Frontend

1. Update personal information in components
2. Modify color scheme in `tailwind.config.js` and `src/app/globals.css`
3. Add your own projects in `src/components/Projects.js`
4. Customize animations in component files

### Backend

1. Update database credentials in `application.properties`
2. Modify entity fields as needed
3. Add new endpoints in controllers
4. Extend services with additional business logic

## Deployment

### Frontend Deployment Options

- Vercel (recommended for Next.js)
- Netlify
- GitHub Pages
- Any static hosting service

### Backend Deployment Options

- AWS Elastic Beanstalk
- Heroku
- Google Cloud Platform
- Azure App Service
- Self-hosted server

### Database Deployment Options

- AWS RDS
- Google Cloud SQL
- Azure Database for MySQL
- Self-hosted MySQL server

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Open a pull request

## License

This project is licensed under the MIT License.

## Acknowledgements

- [Next.js Documentation](https://nextjs.org/docs)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Tailwind CSS Documentation](https://tailwindcss.com/docs)
- [Framer Motion Documentation](https://www.framer.com/motion/)
