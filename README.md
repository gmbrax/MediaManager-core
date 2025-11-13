# MediaManager Core

A Java-based media management system that uses IPC (Inter-Process Communication) with named pipes and PostgreSQL for data persistence.

## Features

- IPC communication using named pipes
- PostgreSQL database integration
- JPA/Hibernate for ORM
- Log4j 2 for logging
- HikariCP connection pooling
- Sample Media entity model

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12 or higher

## Project Structure

```
MediaManager-Core/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── mediamanager/
│       │           ├── config/          # Configuration classes
│       │           ├── model/           # JPA entities
│       │           ├── repository/      # Data access layer
│       │           ├── service/         # Business logic
│       │           ├── ipc/            # IPC implementation
│       │           ├── util/           # Utility classes
│       │           └── MediaManagerApplication.java
│       └── resources/
│           ├── config.properties       # App configuration
│           ├── log4j2.xml              # Logging configuration
│           └── META-INF/
│               └── persistence.xml     # JPA configuration
└── pom.xml
```

## Setup

### 1. Database Setup

Create a PostgreSQL database and user:

```sql
CREATE DATABASE mediamanager;
CREATE USER mediamanager WITH PASSWORD 'changeme';
GRANT ALL PRIVILEGES ON DATABASE mediamanager TO mediamanager;
```

### 2. Configuration

Edit `src/main/resources/config.properties` and update the database credentials:

```properties
db.url=jdbc:postgresql://localhost:5432/mediamanager
db.username=mediamanager
db.password=your_password_here
```

### 3. Build the Project

```bash
mvn clean install
```

### 4. Run the Application

```bash
mvn exec:java -Dexec.mainClass="com.mediamanager.MediaManagerApplication"
```

Or build and run the JAR:

```bash
mvn clean package
java -jar target/MediaManager-Core-0.0.1-SNAPSHOT.jar
```

## IPC Configuration

The application creates named pipes for inter-process communication. Default configuration:

- Pipe path: `/tmp/mediamanager`
- Pipe name: `mediamanager-pipe`
- Buffer size: 8192 bytes

You can modify these settings in `config.properties`.

## Logging

Logs are written to:
- Console (STDOUT)
- `logs/mediamanager.log` (rotating daily, max 10MB per file)

Log configuration can be modified in `src/main/resources/log4j2.xml`.

## Development

### Adding New Entities

1. Create entity class in `com.mediamanager.model`
2. Add the entity class to `persistence.xml`
3. Create corresponding repository and service classes

### Running Tests

```bash
mvn test
```

## Dependencies

- PostgreSQL Driver: 42.7.5
- Hibernate ORM: 7.1.7.Final
- HikariCP: 5.1.0
- Log4j 2: 2.23.1
- Jackson: 2.16.1
- JUnit 5: 5.10.2

## License

TBD

## TODO

- [ ] Implement IPC server with named pipes
- [ ] Implement database connection manager
- [ ] Create repository layer
- [ ] Create service layer
- [ ] Add comprehensive tests
- [ ] Add API documentation
