
# Gym Management System - Spring Boot Project

## Overview
This is a Gym Management System developed using Spring Boot. It allows users to manage bookings, gym classes, and member details. The project is designed to help gyms automate class bookings, manage member participation, and ensure efficient operations.

## Features
- **Class Management**: Ability to create, update, and view gym classes.
- **Booking System**: Allow members to book gym classes.
- **Exception Handling**: Graceful handling of errors like capacity exceeded, invalid dates, and class not found.
- **Search Bookings**: Search for bookings by member name, participation date range, and more.

## Prerequisites
Before running this project, ensure you have the following installed:
- **Java 11+** (preferably Java 17)
- **Maven** (for managing dependencies)
- **IDE** (such as IntelliJ IDEA or Eclipse)
- **Postman or any API testing tool** (for testing API endpoints)

### Optional Tools:
- **Docker** (optional, for running MySQL container, though we are using H2 for this project)
- **Postman** (optional, for testing REST API)

## Setting Up the Project

### Step 1: Clone the Repository

```bash
git clone https://github.com/your-username/gym-management-system.git
```

### Step 2: Configure H2 Database

The project is configured to use **H2** as the in-memory database. It automatically creates the database schema on startup.

1. **Database Configuration**: The `application.properties` file is already set up for H2 database. Here’s the relevant configuration:

   ```properties
   spring.application.name=Gym Management Application

   # H2 Database configuration
   spring.datasource.url=jdbc:h2:mem:clubdb
   spring.datasource.driver-class-name=org.h2.Driver
   spring.datasource.username=sa
   spring.datasource.password=password
   spring.h2.console.enabled=true
   spring.h2.console.path=/h2-console

   # JPA configuration
   spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
   spring.jpa.hibernate.ddl-auto=create-drop
   spring.jpa.show-sql=true

   server.port=8090
   ```

   This configuration will:
   - Use an in-memory H2 database named `clubdb`.
   - Enable the H2 console for easy database access.
   - Configure Hibernate to automatically generate the database schema (create-drop).

### Step 3: Install Dependencies

Navigate to the root directory of the project and run:

```bash
mvn clean install
```

This will download all the required dependencies for the project.

### Step 4: Run the Application

Once the project is set up, you can run the application using Maven:

```bash
mvn spring-boot:run
```

Alternatively, you can run the `GymManagementApplication` class directly from your IDE (e.g., IntelliJ IDEA or Eclipse).

### Step 5: Access the H2 Console

The H2 console is enabled and accessible via `http://localhost:8090/h2-console`. To access the console:

1. Open your browser and navigate to `http://localhost:8090/h2-console`.
2. In the **JDBC URL** field, enter `jdbc:h2:mem:clubdb` (it should already be populated).
3. Username: `sa`
4. Password: `password`

Click **Connect** to access the in-memory H2 database. You’ll be able to view and run SQL queries on the `clubdb` database.

### Step 6: Testing the Application

Once the application is running, you can test the following API endpoints:

#### 1. Create Booking
- **Endpoint**: `POST /api/bookings`
- **Request Body**:
  ```json
  {
    "classId": 1,
    "memberName": "John Doe",
    "participationDate": "2025-01-30"
  }
  ```
- **Description**: Allows a user to create a booking for a specific class on a given date.

#### 2. Search Bookings
- **Endpoint**: `GET /api/bookings`
- **Query Parameters**:
  - `memberName`: (Optional) Filter by member name.
  - `startDate`: (Optional) Filter by start date.
  - `endDate`: (Optional) Filter by end date.
  
Example:
```bash
GET http://localhost:8090/api/bookings?memberName=John%20Doe&startDate=2025-01-01&endDate=2025-01-31
```

#### 3. Error Scenarios
- **Invalid Class ID**: Returns `404 Class not found.` if the class ID doesn't exist.
- **Capacity Exceeded**: Returns `400 Class is already at full capacity.` if the class is fully booked.
- **Invalid Date**: Returns `400 Participation date must be in the future.` if the date is in the past.

---

## Project Structure

```
gym-management-system
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/gymmanagement/
│   │   │       ├── controller/
│   │   │       ├── dto/
│   │   │       ├── entity/
│   │   │       ├── exception/
│   │   │       ├── repository/
│   │   │       └── service/
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/
│   │       └── templates/
│   └── test/
│       └── java/com/gymmanagement/
│           └── service/BookingServiceTest.java
│
└── pom.xml
```

- **Controller**: Contains REST API controllers to handle incoming requests.
- **DTO**: Contains Data Transfer Objects used for request and response mapping.
- **Entity**: Contains the JPA entities (`Booking`, `ClubClass`).
- **Service**: Contains the business logic for bookings and classes.
- **Repository**: Contains Spring Data JPA repositories to interact with the database.

---

## Common Issues

### Issue: `Could not open JPA EntityManager`
- **Solution**: Ensure the H2 database is correctly configured in `application.properties`. Make sure the application is running on the correct port (`8090`).

### Issue: `No Class Def Found: org/springframework/boot/spring-boot-starter-web`
- **Solution**: Try running `mvn clean install` again to re-download dependencies.

---

## License

This project is licensed under the MIT License.
