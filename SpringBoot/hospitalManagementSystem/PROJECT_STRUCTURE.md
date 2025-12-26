# 📁 Project Structure

Visual representation of the Hospital Management System project structure.

---

## 🗂️ Root Directory

```
hospitalManagementSystem/
│
├── 📄 README.md                          # Main project documentation
├── 📄 QUICK_START.md                     # ⚡ Fast setup guide (NEW)
├── 📄 STARTUP_GUIDE.md                   # 🚀 Complete startup instructions (NEW)
├── 📄 DATABASE_SETUP.md                  # 🗄️ Database configuration guide (NEW)
├── 📄 NEW_ADDITIONS_SUMMARY.md           # 📝 Summary of new additions (NEW)
├── 📄 JWT_AUTHENTICATION_GUIDE.md        # 🔐 Authentication documentation
├── 📄 COMPREHENSIVE_IMPROVEMENTS.md      # 📋 System improvements
├── 📄 ISSUES.md                          # 🐛 Known issues
│
├── 🔧 start-backend.sh                   # Backend startup script (Linux/macOS) (NEW)
├── 🔧 start-backend.bat                  # Backend startup script (Windows) (NEW)
├── 🔧 start-frontend.sh                  # Frontend startup script (Linux/macOS) (NEW)
├── 🔧 start-frontend.bat                 # Frontend startup script (Windows) (NEW)
│
├── 📁 hospital/                          # Backend (Spring Boot)
│   ├── 📁 src/
│   │   ├── 📁 main/
│   │   │   ├── 📁 java/com/pacman/hospital/
│   │   │   │   ├── 📁 config/           # Configuration classes
│   │   │   │   ├── 📁 controller/       # REST controllers
│   │   │   │   ├── 📁 service/          # Business logic
│   │   │   │   ├── 📁 repository/       # Data access
│   │   │   │   ├── 📁 model/            # Entity models
│   │   │   │   ├── 📁 dto/              # Data transfer objects
│   │   │   │   ├── 📁 security/         # Security & JWT
│   │   │   │   ├── 📁 exception/        # Exception handling
│   │   │   │   └── 📄 HospitalApplication.java
│   │   │   │
│   │   │   └── 📁 resources/
│   │   │       ├── 📄 application.properties  # Backend config
│   │   │       └── 📁 db/migration/          # Flyway migrations
│   │   │           ├── V1__alter_appointment_bill_doctor_patient_insurance_lab_medicalRec_medicine_prescriptions.sql
│   │   │           ├── V2__alter_lab_ord_&_Create_User_Tables.sql
│   │   │           ├── V3__insurance.sql
│   │   │           ├── V4__invoice.sql
│   │   │           ├── V5__alter_table_invoice_status.sql
│   │   │           └── V6__Payments.sql
│   │   │
│   │   └── 📁 test/                     # Unit & integration tests
│   │
│   ├── 📄 pom.xml                       # Maven dependencies
│   ├── 📄 mvnw                          # Maven wrapper (Unix)
│   ├── 📄 mvnw.cmd                      # Maven wrapper (Windows)
│   └── 📁 target/                       # Build output
│
├── 📁 frontend/                         # Frontend (Next.js)
│   ├── 📁 src/
│   │   ├── 📁 app/                      # Next.js 16 App Router
│   │   │   ├── 📁 (auth)/              # Authentication routes
│   │   │   ├── 📁 (dashboard)/         # Dashboard routes
│   │   │   ├── 📁 api/                 # API routes (if any)
│   │   │   ├── 📄 layout.tsx           # Root layout
│   │   │   ├── 📄 page.tsx             # Home page
│   │   │   └── 📄 globals.css          # Global styles
│   │   │
│   │   ├── 📁 components/              # React components
│   │   │   ├── 📁 ui/                  # UI components
│   │   │   ├── 📁 forms/               # Form components
│   │   │   ├── 📁 layout/              # Layout components
│   │   │   └── 📁 dashboard/           # Dashboard components
│   │   │
│   │   ├── 📁 lib/                     # Utility libraries
│   │   │   ├── 📄 api.ts               # API client
│   │   │   ├── 📄 auth.ts              # Authentication utilities
│   │   │   └── 📄 utils.ts             # Helper functions
│   │   │
│   │   ├── 📁 hooks/                   # Custom React hooks
│   │   ├── 📁 contexts/                # React contexts
│   │   ├── 📁 types/                   # TypeScript types
│   │   └── 📁 styles/                  # Styling files
│   │
│   ├── 📁 public/                      # Static assets
│   ├── 📄 package.json                 # npm dependencies
│   ├── 📄 tsconfig.json                # TypeScript config
│   ├── 📄 next.config.ts               # Next.js config
│   ├── 📄 tailwind.config.js           # Tailwind CSS config
│   ├── 📄 .env.local                   # Environment variables (create this)
│   └── 📁 node_modules/                # npm packages
│
└── 📁 uploads/                         # File upload storage
```

---

## 🎯 Key Directories Explained

### Backend (hospital/)

| Directory | Purpose |
|-----------|---------|
| `src/main/java/` | Java source code |
| `src/main/resources/` | Configuration & migrations |
| `src/test/` | Test files |
| `target/` | Compiled output |

### Frontend (frontend/)

| Directory | Purpose |
|-----------|---------|
| `src/app/` | Next.js pages & routes |
| `src/components/` | Reusable React components |
| `src/lib/` | Utilities & API clients |
| `src/hooks/` | Custom React hooks |
| `public/` | Static files |

---

## 📄 New Files Added (This Update)

### Startup Scripts (4 files)
- ✅ `start-backend.sh` - Backend startup (Linux/macOS)
- ✅ `start-backend.bat` - Backend startup (Windows)
- ✅ `start-frontend.sh` - Frontend startup (Linux/macOS)
- ✅ `start-frontend.bat` - Frontend startup (Windows)

### Documentation (4 files)
- ✅ `QUICK_START.md` - Fast setup guide
- ✅ `STARTUP_GUIDE.md` - Complete startup instructions
- ✅ `DATABASE_SETUP.md` - Database configuration
- ✅ `NEW_ADDITIONS_SUMMARY.md` - Update summary

---

## 🔧 Configuration Files

### Backend Configuration

```
hospital/src/main/resources/application.properties
└── Contains:
    ├── Database connection (MySQL)
    ├── Server port (8080)
    ├── JWT secret & expiration
    ├── Flyway settings
    ├── File upload settings
    └── Logging configuration
```

### Frontend Configuration

```
frontend/.env.local (create this file)
└── Contains:
    ├── API base URL
    ├── App name & version
    ├── Feature flags
    └── File upload limits
```

---

## 🗄️ Database Structure

```
MySQL (hospital_db)
├── users                    # System users
├── roles                    # User roles
├── user_roles               # User-role mapping
├── patients                 # Patient information
├── doctors                  # Doctor information
├── appointments             # Appointment scheduling
├── medical_documents        # Document management
├── prescriptions            # Medication prescriptions
├── lab_tests                # Lab test definitions
├── lab_orders               # Lab test orders
├── billings                 # Billing records
├── invoices                 # Invoice management
├── payments                 # Payment records
└── flyway_schema_history    # Migration tracking
```

---

## 🚀 Startup Flow

```
1. Database Setup
   └── Run: DATABASE_SETUP.md instructions
       └── Create database & user
           └── Update application.properties

2. Start Backend
   └── Run: ./start-backend.sh (or .bat)
       └── Checks: Java, Maven, MySQL
           └── Starts: Spring Boot on :8080
               └── Ready: Swagger UI accessible

3. Start Frontend
   └── Run: ./start-frontend.sh (or .bat)
       └── Checks: Node.js, npm
           └── Installs: Dependencies (if needed)
               └── Starts: Next.js on :3000
                   └── Ready: Login page accessible

4. Access Application
   └── Frontend: http://localhost:3000
   └── Backend: http://localhost:8080
   └── API Docs: http://localhost:8080/swagger-ui.html
```

---

## 📊 Technology Stack

### Backend Stack
```
hospital/
├── Java 17+
├── Spring Boot 3.5.6
│   ├── Spring Web
│   ├── Spring Data JPA
│   ├── Spring Security
│   └── Spring Validation
├── MySQL 8.0+
├── Flyway (migrations)
├── JWT (authentication)
├── MapStruct (mapping)
├── Lombok (boilerplate)
└── Swagger/OpenAPI (docs)
```

### Frontend Stack
```
frontend/
├── Node.js 18+
├── Next.js 16.0.0
├── React 19.2.0
├── TypeScript 5.6+
├── Tailwind CSS 4.0
├── React Query (state)
├── React Hook Form (forms)
├── Zod (validation)
├── Axios (HTTP)
└── Lucide React (icons)
```

---

## 🔗 Port Usage

| Service | Port | Configurable |
|---------|------|--------------|
| Frontend | 3000 | Yes (Next.js auto-selects if busy) |
| Backend | 8080 | Yes (application.properties) |
| MySQL | 3306 | Yes (MySQL config) |

---

## 📖 Documentation Map

```
Documentation Structure:
│
├── 📄 README.md                          # Start here
├── 📄 QUICK_START.md                     # Get running fast
├── 📄 STARTUP_GUIDE.md                   # Complete startup guide
├── 📄 DATABASE_SETUP.md                  # Database configuration
├── 📄 JWT_AUTHENTICATION_GUIDE.md        # Security & auth
├── 📄 COMPREHENSIVE_IMPROVEMENTS.md      # System details
└── 📄 NEW_ADDITIONS_SUMMARY.md           # Recent updates
```

**Navigation**:
- New to project? → Start with `README.md` then `QUICK_START.md`
- Setting up database? → Read `DATABASE_SETUP.md`
- Running the app? → Use `STARTUP_GUIDE.md`
- Understanding auth? → See `JWT_AUTHENTICATION_GUIDE.md`
- Need details? → Check `COMPREHENSIVE_IMPROVEMENTS.md`

---

## 🎯 Development Workflow

```
Developer's Daily Routine:
│
1. Start MySQL
   └── sudo systemctl start mysql (Linux)
   └── brew services start mysql@8.0 (macOS)
   └── net start MySQL80 (Windows)
│
2. Start Backend (Terminal 1)
   └── ./start-backend.sh
   └── Wait for: "Started HospitalApplication"
│
3. Start Frontend (Terminal 2)
   └── ./start-frontend.sh
   └── Wait for: "✓ Ready in Xs"
│
4. Open Browser
   └── http://localhost:3000
   └── Login with credentials
│
5. Develop & Test
   └── Make changes
   └── Hot reload active
   └── Check logs
│
6. Stop Services
   └── Ctrl+C in both terminals
```

---

## 🔍 Finding Files

### Common File Locations

| What | Where |
|------|-------|
| Main backend code | `hospital/src/main/java/com/pacman/hospital/` |
| REST controllers | `hospital/src/main/java/com/pacman/hospital/controller/` |
| Database migrations | `hospital/src/main/resources/db/migration/` |
| Backend config | `hospital/src/main/resources/application.properties` |
| Frontend pages | `frontend/src/app/` |
| React components | `frontend/src/components/` |
| API client | `frontend/src/lib/api.ts` |
| Frontend config | `frontend/.env.local` |
| Startup scripts | Root directory (`./start-*.sh`, `.bat`) |
| Documentation | Root directory (`*.md` files) |

---

## 📝 Quick Commands Reference

```bash
# Navigate to project root
cd hospitalManagementSystem

# Start services
./start-backend.sh          # Backend (Linux/macOS)
./start-frontend.sh         # Frontend (Linux/macOS)
start-backend.bat           # Backend (Windows)
start-frontend.bat          # Frontend (Windows)

# Manual start (alternative)
cd hospital && ./mvnw spring-boot:run     # Backend
cd frontend && npm run dev                 # Frontend

# Database access
mysql -u hospital_user -p hospital_db

# Check running services
lsof -i :8080              # Backend
lsof -i :3000              # Frontend
lsof -i :3306              # MySQL

# View logs
tail -f hospital/logs/hospital.log        # Backend logs (if configured)
# Frontend logs are in terminal output
```

---

## 🎓 Learning Path

### For New Developers:

1. **Day 1**: Setup & Exploration
   - Read `README.md`
   - Follow `QUICK_START.md`
   - Setup database using `DATABASE_SETUP.md`
   - Run application with startup scripts
   - Explore Swagger UI

2. **Day 2**: Understanding Architecture
   - Review project structure (this file)
   - Read `COMPREHENSIVE_IMPROVEMENTS.md`
   - Explore backend code structure
   - Explore frontend code structure

3. **Day 3**: Security & Features
   - Study `JWT_AUTHENTICATION_GUIDE.md`
   - Test different user roles
   - Review API endpoints
   - Test key features

4. **Day 4+**: Development
   - Start coding
   - Follow existing patterns
   - Run tests
   - Commit changes

---

**Version**: 2.0.0  
**Last Updated**: January 2025  
**Created By**: Hospital Management System Team