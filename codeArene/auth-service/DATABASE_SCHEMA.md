# Database Schema Documentation

## Overview

This document describes the database schema for the CodeArena Authentication Service. The service uses MySQL as the primary database with Flyway for database migrations.

## Migration Strategy

- **Migration Tool**: Flyway
- **Migration Location**: `src/main/resources/db/migration/`
- **Naming Convention**: `V{version}__{description}.sql`
- **Database**: MySQL 8.0+

## Database Configuration

### Development Environment
- **Database**: `codearena_auth`
- **Host**: `localhost:3306`
- **Connection URL**: `jdbc:mysql://localhost:3306/codearena_auth?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true`

### Test Environment
- **Database**: H2 In-Memory
- **Connection URL**: `jdbc:h2:mem:testdb`

## Schema Structure

### Users Table

**Table Name**: `users`

| Column Name | Data Type | Constraints | Description |
|-------------|-----------|-------------|-------------|
| `id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique identifier for the user |
| `username` | VARCHAR(50) | NOT NULL, UNIQUE | User's unique username |
| `email` | VARCHAR(255) | NOT NULL, UNIQUE | User's email address |
| `password` | VARCHAR(255) | NOT NULL | BCrypt hashed password |
| `first_name` | VARCHAR(100) | NOT NULL | User's first name |
| `last_name` | VARCHAR(100) | NOT NULL | User's last name |
| `role` | ENUM('USER', 'ADMIN', 'MODERATOR') | NOT NULL, DEFAULT 'USER' | User's role in the system |
| `is_enabled` | BOOLEAN | NOT NULL, DEFAULT TRUE | Whether the user account is enabled |
| `is_account_non_expired` | BOOLEAN | NOT NULL, DEFAULT TRUE | Whether the account is not expired |
| `is_account_non_locked` | BOOLEAN | NOT NULL, DEFAULT TRUE | Whether the account is not locked |
| `is_credentials_non_expired` | BOOLEAN | NOT NULL, DEFAULT TRUE | Whether the credentials are not expired |
| `created_at` | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Account creation timestamp |
| `updated_at` | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Last update timestamp |
| `last_login` | TIMESTAMP | NULL | Last login timestamp |

### Indexes

The following indexes are created for optimal query performance:

| Index Name | Columns | Type | Purpose |
|------------|---------|------|---------|
| `idx_username` | `username` | BTREE | Fast username lookups |
| `idx_email` | `email` | BTREE | Fast email lookups |
| `idx_role` | `role` | BTREE | Role-based queries |
| `idx_created_at` | `created_at` | BTREE | Time-based queries and sorting |

## Migration Files

### V1__Create_users_table.sql

Creates the initial `users` table with all necessary columns, constraints, and indexes.

**Key Features:**
- Auto-incrementing primary key
- Unique constraints on username and email
- ENUM for role management
- Boolean fields with proper defaults
- Timestamp fields with automatic updates
- Performance indexes

### V2__Insert_seed_data.sql

Inserts initial seed data for development and testing purposes.

**Seed Users:**

| Username | Email | Role | Password (Plain) |
|----------|--------|------|------------------|
| admin | admin@codearena.com | ADMIN | password123 |
| moderator | moderator@codearena.com | MODERATOR | password123 |
| testuser | testuser@codearena.com | USER | password123 |
| alice.smith | alice.smith@example.com | USER | password123 |
| bob.johnson | bob.johnson@example.com | USER | password123 |
| charlie.brown | charlie.brown@example.com | USER | password123 |
| diana.prince | diana.prince@example.com | MODERATOR | password123 |
| eve.adams | eve.adams@example.com | USER | password123 |

**Note**: All passwords are BCrypt hashed with strength 10.

## User Roles

### Role Hierarchy

1. **USER** (Default)
   - Standard user privileges
   - Can view own profile
   - Can update own password

2. **MODERATOR**
   - All USER privileges
   - Can view other users
   - Can view user statistics (limited)

3. **ADMIN**
   - All MODERATOR privileges
   - Can manage users (enable/disable/lock/unlock)
   - Can change user roles
   - Can delete users
   - Full access to user statistics

## Security Features

### Password Security
- **Hashing**: BCrypt with strength 10
- **Salt**: Automatically handled by BCrypt
- **Storage**: Never store plain text passwords

### Account Status Management
- **Enabled/Disabled**: Control account access
- **Locked/Unlocked**: Temporary access control
- **Expiration**: Account and credential expiration support

### Audit Trail
- **Created At**: Track account creation
- **Updated At**: Track last modification
- **Last Login**: Track user activity

## JPA Entity Mapping

The database schema is mapped to the `User` entity class with the following annotations:

```java
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;
    
    // ... other fields
}
```

## Database Constraints

### Primary Key Constraints
- `users.id`: Auto-incrementing BIGINT primary key

### Unique Constraints
- `users.username`: Ensures username uniqueness
- `users.email`: Ensures email uniqueness

### Foreign Key Constraints
- Currently none (single table design)

### Check Constraints
- Role ENUM ensures valid role values only

## Performance Considerations

### Indexing Strategy
1. **Primary Access Patterns**: Username and email lookups
2. **Secondary Access Patterns**: Role-based queries
3. **Sorting Requirements**: Creation date sorting

### Query Optimization
- Use indexed columns in WHERE clauses
- Avoid SELECT * in application code
- Use appropriate page sizes for pagination

## Backup and Recovery

### Backup Strategy
1. **Full Backup**: Daily at 2 AM
2. **Incremental Backup**: Every 6 hours
3. **Transaction Log Backup**: Every 15 minutes

### Recovery Considerations
- Point-in-time recovery capability
- Binary log retention: 7 days
- Backup retention: 30 days

## Future Schema Evolution

### Planned Extensions
1. **User Profiles**: Extended user information
2. **OAuth Integration**: External provider support
3. **Session Management**: Active session tracking
4. **Audit Logging**: Detailed activity logs

### Migration Best Practices
1. Always use versioned migrations
2. Test migrations on copy of production data
3. Plan for rollback scenarios
4. Document all schema changes

## Monitoring and Maintenance

### Key Metrics to Monitor
- Table size growth
- Index usage statistics
- Query performance
- Constraint violations

### Maintenance Tasks
- Regular index optimization
- Statistics updates
- Cleanup of old data (if applicable)
- Monitor for unused indexes

## Troubleshooting

### Common Issues
1. **Duplicate Key Errors**: Check for existing usernames/emails
2. **Migration Failures**: Verify data consistency
3. **Performance Issues**: Check index usage
4. **Connection Issues**: Verify database connectivity

### Diagnostic Queries

```sql
-- Check user counts by role
SELECT role, COUNT(*) as count FROM users GROUP BY role;

-- Find recent users
SELECT username, email, created_at FROM users 
ORDER BY created_at DESC LIMIT 10;

-- Check for locked accounts
SELECT username, email FROM users 
WHERE is_account_non_locked = FALSE;

-- Monitor table size
SELECT 
    table_name,
    ROUND(((data_length + index_length) / 1024 / 1024), 2) as size_mb
FROM information_schema.tables 
WHERE table_schema = 'codearena_auth';
```
