# 🏥 Hospital Management System

A comprehensive, full-stack Hospital Management System built with Spring Boot and Next.js, featuring role-based access control for patients, doctors, and administrators.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Quick Start](#quick-start)
- [Project Structure](#project-structure)
- [User Roles & Permissions](#user-roles--permissions)
- [API Documentation](#api-documentation)
- [Configuration](#configuration)
- [Deployment](#deployment)
- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)

## 🎯 Overview

The Hospital Management System is a modern, production-ready application designed to streamline healthcare operations. It provides comprehensive tools for managing patient records, appointments, prescriptions, lab orders, billing, and more.

### Key Highlights

- **Multi-Role Support**: Patient, Doctor, Admin portals with role-specific features
- **Secure Authentication**: JWT-based authentication with role-based access control
- **Real-Time Updates**: Modern, responsive UI with real-time data synchronization
- **Comprehensive Medical Records**: Complete patient history, lab results, prescriptions
- **Billing & Insurance**: Integrated billing system with insurance coverage calculation
- **Health Tracking**: Personal health metrics monitoring for patients
- **File Management**: Secure document upload and management system

## ✨ Features

### Patient Portal
- 📅 **Appointments Management**: Book, view, and manage appointments
- 📋 **Medical Records**: Access complete medical history and records
- 💊 **Prescriptions**: View current and past prescriptions
- 🧪 **Lab Reports**: Access and download lab test results
- 💰 **Billing**: View bills, payment history, and insurance coverage
- ❤️ **Health Tracker**: Monitor vital signs and health metrics
- ⚙️ **Settings**: Manage profile, security, and notification preferences

### Doctor Portal
- 📆 **Schedule Management**: View and manage appointment schedules
- 👥 **Patient Management**: Access patient information and history
- 📝 **Medical Records**: Create and update patient medical records
- 💊 **Prescriptions**: Issue and manage prescriptions
- 🧪 **Lab Requests**: Order lab tests and view results
- 📊 **Dashboard**: Overview of appointments and patient statistics

### Admin Portal
- 👨‍⚕️ **Staff Management**: Manage doctors and medical staff
- 👤 **Patient Management**: Oversee patient registrations and records
- 📅 **Appointment Oversight**: Monitor and manage all appointments
- 💵 **Billing Administration**: Manage billing, payments, and invoices
- 🏥 **Department Management**: Organize hospital departments
- 📊 **Reports & Analytics**: Comprehensive reporting and insights

## 🛠️ Technology Stack

### Backend
- **Framework**: Spring Boot 3.5.x
- **Language**: Java 17
- **Database**: MySQL 8.0
- **ORM**: Spring Data JPA / Hibernate
- **Migration**: Flyway
- **Security**: Spring Security + JWT
- **Validation**: Jakarta Bean Validation
- **Mapping**: MapStruct
- **Documentation**: SpringDoc OpenAPI (Swagger)
- **Build Tool**: Maven

### Frontend
- **Framework**: Next.js 14 (App Router)
- **Language**: TypeScript
- **UI Library**: React 18
- **Styling**: Tailwind CSS
- **Icons**: Lucide React
- **HTTP Client**: Axios
- **State Management**: React Hooks
- **Forms**: React Hook Form (coming)
- **Notifications**: React Hot Toast
- **Authentication**: Cookie-based JWT storage

## 🚀 Quick Start

### Prerequisites

- Java 17 or higher
- Node.js 18+ and npm/yarn
- MySQL 8.0+
- Git

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd hospitalManagementSystem
   ```

2. **Setup Database**
   ```sql
   CREATE DATABASE hospital;
   CREATE USER 'hospital_user'@'localhost' IDENTIFIED BY 'your_password';
   GRANT ALL PRIVILEGES ON hospital.* TO 'hospital_user'@'localhost';
   FLUSH PRIVILEGES;
   ```

3. **Configure Backend**
   
   Edit `hospital/src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/hospital
   spring.datasource.username=hospital_user
   spring.datasource.password=your_password
   
   # JWT Configuration
   jwt.secret=your-256-bit-secret-key-here
   jwt.expiration=86400000
   
   # File Storage
   storage.local.base-dir=uploads
   storage.local.uploads-dir=uploads
   ```

4. **Start Backend**
   ```bash
   cd hospital
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```
   
   Backend will start at: `http://localhost:8080`
   Swagger UI: `http://localhost:8080/swagger-ui.html`

5. **Configure Frontend**
   
   Create `frontend/.env.local`:
   ```env
   NEXT_PUBLIC_API_BASE_URL=http://localhost:8080
   ```

6. **Start Frontend**
   ```bash
   cd frontend
   npm install
   npm run dev
   ```
   
   Frontend will start at: `http://localhost:3000`

### Default Credentials

After running migrations and seed data, use these credentials:

**Admin Account:**
- Username: `admin`
- Password: `admin123`

**Doctor Account:**
- Username: `doctor`
- Password: `doctor123`

**Patient Account:**
- Username: `patient`
- Password: `patient123`

> ⚠️ **Security Warning**: Change these default passwords immediately in production!

## 📁 Project Structure

```
hospitalManagementSystem/
├── hospital/                          # Backend (Spring Boot)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/pacman/hospital/
│   │   │   │   ├── config/           # Configuration classes
│   │   │   │   ├── core/
│   │   │   │   │   └── security/     # Security & JWT implementation
│   │   │   │   ├── domain/           # Domain modules
│   │   │   │   │   ├── appointment/  # Appointment management
│   │   │   │   │   ├── billing/      # Billing & payments
│   │   │   │   │   ├── doctor/       # Doctor management
│   │   │   │   │   ├── insurance/    # Insurance handling
│   │   │   │   │   ├── laboratory/   # Lab tests & orders
│   │   │   │   │   ├── medicalrecord/# Medical records
│   │   │   │   │   ├── patient/      # Patient management
│   │   │   │   │   ├── pharmacy/     # Medicines & prescriptions
│   │   │   │   │   └── ...
│   │   │   │   ├── common/           # Shared utilities
│   │   │   │   └── exception/        # Exception handling
│   │   │   └── resources/
│   │   │       ├── db/migration/     # Flyway migrations
│   │   │       └── application.properties
│   │   └── test/                     # Tests
│   └── pom.xml
│
└── frontend/                         # Frontend (Next.js)
    ├── src/
    │   ├── app/                      # App Router pages
    │   │   ├── login/               # Authentication
    │   │   ├── dashboard/           # Main dashboard
    │   │   ├── my-appointments/     # Patient appointments
    │   │   ├── my-records/          # Medical records
    │   │   ├── my-prescriptions/    # Prescriptions
    │   │   ├── my-lab-reports/      # Lab reports
    │   │   ├── my-billing/          # Billing management
    │   │   ├── health-tracker/      # Health tracking
    │   │   ├── settings/            # User settings
    │   │   └── ...
    │   ├── components/              # Reusable components
    │   │   ├── layout/             # Layout components
    │   │   └── ui/                 # UI components
    │   ├── providers/              # Context providers
    │   ├── services/               # API services
    │   │   └── api.ts             # API client
    │   └── types/                  # TypeScript types
    ├── public/                     # Static assets
    └── package.json
```

## 👥 User Roles & Permissions

### Patient
- View and manage own appointments
- Access personal medical records
- View prescriptions and medications
- Access lab test results
- View and manage billing
- Track personal health metrics
- Update profile and settings

### Doctor
- View and manage schedules
- Access patient medical records
- Create/update medical records
- Issue prescriptions
- Order lab tests
- View lab results
- Manage appointments

### Admin
- Full system access
- Manage all users (patients, doctors, staff)
- Oversee all appointments
- Billing administration
- System configuration
- Generate reports
- Department management

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api
```

### Authentication Endpoints

```http
POST /api/auth/login
POST /api/auth/register
POST /api/auth/refresh
```

### Patient Endpoints

```http
GET    /api/patients
GET    /api/patients/{id}
POST   /api/patients
PUT    /api/patients/{id}
DELETE /api/patients/{id}
GET    /api/patients/page?q=&page=0&size=20
```

### Appointment Endpoints

```http
GET    /api/appointments
GET    /api/appointments/{id}
POST   /api/appointments
PUT    /api/appointments/{id}
DELETE /api/appointments/{id}
GET    /api/appointments/patient/{patientId}
GET    /api/appointments/doctor/{doctorId}
```

### Medical Records Endpoints

```http
GET    /api/medical-records
GET    /api/medical-records/{id}
POST   /api/medical-records
PUT    /api/medical-records/{id}
DELETE /api/medical-records/{id}
GET    /api/medical-records/patient/{patientId}
```

### Prescription Endpoints

```http
GET    /api/prescriptions
GET    /api/prescriptions/{id}
POST   /api/prescriptions
PUT    /api/prescriptions/{id}
DELETE /api/prescriptions/{id}
GET    /api/prescriptions/patient/{patientId}
```

### Lab Orders Endpoints

```http
GET    /api/lab-orders
GET    /api/lab-orders/{id}
POST   /api/lab-orders
PUT    /api/lab-orders/{id}
DELETE /api/lab-orders/{id}
GET    /api/lab-orders/patient/{patientId}
```

### Billing Endpoints

```http
GET    /api/billings
GET    /api/billings/{id}
POST   /api/billings
PUT    /api/billings/{id}
DELETE /api/billings/{id}
GET    /api/billings/patient/{patientId}
POST   /api/billings/{id}/pay
```

### Authentication

All protected endpoints require JWT token in the Authorization header:

```http
Authorization: Bearer <your-jwt-token>
```

For detailed API documentation, visit the Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

## ⚙️ Configuration

### Backend Configuration

**application.properties**
```properties
# Server Configuration
server.port=8080

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/hospital
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true

# Flyway Configuration
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true

# JWT Configuration
jwt.secret=your-256-bit-secret-key
jwt.expiration=86400000

# File Upload Configuration
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
storage.local.base-dir=uploads
storage.local.uploads-dir=uploads

# Logging
logging.level.com.pacman.hospital=DEBUG
```

### Frontend Configuration

**.env.local**
```env
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080
```

## 🐳 Deployment

### Docker Deployment

1. **Build Docker Images**
   ```bash
   # Backend
   cd hospital
   docker build -t hospital-backend .
   
   # Frontend
   cd frontend
   docker build -t hospital-frontend .
   ```

2. **Run with Docker Compose**
   ```bash
   docker-compose up -d
   ```

### Production Deployment

1. **Backend (JAR)**
   ```bash
   cd hospital
   ./mvnw clean package -DskipTests
   java -jar target/hospital-0.0.1-SNAPSHOT.jar
   ```

2. **Frontend (Static Export)**
   ```bash
   cd frontend
   npm run build
   npm start
   ```

### Environment Variables

**Backend:**
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `JWT_SECRET`
- `JWT_EXPIRATION`

**Frontend:**
- `NEXT_PUBLIC_API_BASE_URL`

## 🧪 Testing

### Backend Tests

```bash
cd hospital
./mvnw test
./mvnw verify
```

### Frontend Tests

```bash
cd frontend
npm test
npm run test:e2e
```

## 🔒 Security

### Features
- JWT-based authentication
- Role-based access control (RBAC)
- Password encryption (BCrypt)
- CORS configuration
- SQL injection prevention (JPA)
- XSS protection
- CSRF protection
- Secure file upload handling

### Best Practices
- Always use HTTPS in production
- Rotate JWT secrets regularly
- Implement rate limiting
- Use secure session management
- Regular security audits
- Keep dependencies updated

## 📊 Database Schema

### Core Tables
- `users` - System users (patients, doctors, admins)
- `roles` - User roles
- `user_roles` - User-role mapping
- `patients` - Patient information
- `doctors` - Doctor information
- `appointments` - Appointment scheduling
- `medical_records` - Patient medical records
- `prescriptions` - Medicine prescriptions
- `medicines` - Medicine inventory
- `lab_orders` - Lab test orders
- `lab_tests` - Lab test definitions
- `billings` - Billing records
- `insurances` - Insurance policies
- `documents` - Medical documents

## 🐛 Known Issues & Limitations

### Current Limitations
- File storage is local (not cloud-based)
- No real-time notifications (WebSocket)
- Limited reporting capabilities
- No appointment reminders via SMS/Email
- Health tracker data is not persistent yet

### Planned Features
- Video consultation integration
- Advanced analytics dashboard
- Mobile application
- Electronic prescription system
- Integration with external lab systems
- Payment gateway integration
- Multi-language support

## 🤝 Contributing

We welcome contributions! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Development Guidelines
- Follow existing code style and conventions
- Write meaningful commit messages
- Add tests for new features
- Update documentation as needed
- Ensure all tests pass before submitting PR

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📞 Support

For support and questions:
- **Email**: support@hospital-system.com
- **Issues**: GitHub Issues
- **Documentation**: [Wiki](https://github.com/your-repo/wiki)

## 🙏 Acknowledgments

- Spring Boot community
- Next.js team
- All contributors and testers
- Open source libraries used in this project

## 📈 Project Statistics

- **Lines of Code**: ~50,000+
- **API Endpoints**: 100+
- **Database Tables**: 20+
- **UI Components**: 50+
- **Test Coverage**: Work in Progress

---

**Built with ❤️ for better healthcare management**

Last Updated: December 2024