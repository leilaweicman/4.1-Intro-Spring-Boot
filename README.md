# S4.01 - User API 🧑‍💻

## 📄 Description

This project is a **Spring Boot REST API** that manages users.  
It demonstrates the use of the **Repository Pattern**, **Service Layer**, and **Test-Driven Development (TDD)** with **JUnit** and **Mockito**.

The API allows creating, retrieving, and searching users while validating unique email addresses.  
The project follows a layered architecture with clear separation between Controller, Service, and Repository layers.

---

## 💻 Technologies Used

- Java 17+
- Spring Boot 3
- Spring Web
- JUnit 5
- Mockito
- Maven
- IntelliJ IDEA / VS Code

---

## 📋 Requirements

- Java 17 or higher
- Maven 3.9+
- Git

---

## 🛠️ Installation

1. Clone this repository
```bash
git clone https://github.com/leilaweicman/4.1-Intro-Spring-Boot.git
```

2. Navigate to the project folder
```bash
cd user-api
```

3. Build the project
```bash
mvn clean install
```

---

## ▶️ Running the Application

Run the application using Maven:
```bash
mvn spring-boot:run
```
Once started, the API will be available at: 
http://localhost:8080

### 🧭 Available Endpoints

| Method | Endpoint           | Description                      |
|---------|--------------------|----------------------------------|
| `GET`   | `/health`          | Returns API health status        |
| `GET`   | `/users`           | Retrieves all users              |
| `GET`   | `/users?name=John` | Retrieves users filtered by name |
| `GET`   | `/users/{id}`      | Retrieves user by UUID           |
| `POST`  | `/users`           | Creates a new user               |

---

## 🧠 Architecture Overview

This project follows a **three-layer architecture**:

1. **Controller Layer**  
   Handles HTTP requests and responses.  
   Example: `UserController`

2. **Service Layer**  
   Contains business logic (e.g., email validation).  
   Example: `UserServiceImpl`

3. **Repository Layer**  
   Handles data persistence (in-memory for this exercise).  
   Example: `InMemoryUserRepository`

### Sequence Diagram (Create User)

```
Client → Controller → Service → Repository
                ↑          ↓
         Exception     Save or Throw EmailAlreadyExistsException
```

**Explanation:**
1. The **Client** sends a `POST /users` request with user data.
2. The **Controller** receives the request and delegates it to the **Service Layer**.
3. The **Service** checks if the email already exists through the **Repository**.
4. If the email exists → throws `EmailAlreadyExistsException` (returns HTTP 409 Conflict).
5. If not → generates a new `UUID`, saves the user via the **Repository**, and returns the created user (`HTTP 201 Created`).

---

## 🧪 Testing

Testing in this project is divided into three main parts: **Controller**, **Repository**, and **Service**.  
All tests use **JUnit 5**, **Spring Boot Test**, and **Mockito**.

---

### ✅ Controller Tests (Integration)

**Class:** `UserControllerTest`  
These tests use **MockMvc** to simulate HTTP requests and verify API responses without running a real server.

**Test cases include:**
- `GET /users` → Returns an empty list initially
- `POST /users` → Creates a new user (`201 Created`)
- `POST /users` with existing email → Returns `409 Conflict`
- `GET /users/{id}` → Returns a specific user (`200 OK`)
- `GET /users/{id}` with invalid ID → Returns `404 Not Found`
- `GET /users?name=John` → Filters users by name (case-insensitive)

---

### ✅ Repository Tests (Unit)

**Class:** `InMemoryUserRepositoryTest`  
These tests ensure that the repository correctly handles user storage and retrieval in memory.

**Covered scenarios:**
- Saving a user adds it to the list
- Finding all users returns the correct collection
- Searching by ID returns the right user
- Searching by name filters correctly
- Checking if an email already exists works as expected

---

### ✅ Service Tests (Unit with Mockito)

**Class:** `UserServiceImplTest`  
These tests isolate the service layer by mocking the repository using **Mockito**.

**Key business logic tested:**
- Throws `EmailAlreadyExistsException` when trying to create a user with an existing email
- Successfully creates a user when email is unique
- Generates a valid UUID before saving
- Calls `userRepository.save()` exactly once when valid

---


## 🌐 Deployment
1. Build the JAR:
```bash
mvn clean package
```

2. Run the JAR file:
```bash
java -jar target/userapi-0.0.1-SNAPSHOT.jar
```


---

## 🤝 Contributions

1. Fork this repository.
2. Create a new branch:
```sh
git checkout -b feature/NewFeature
```
3. Commit your changes:
```sh
git commit -m "Add NewFeature"
```
4. Commit your changes:
```sh
git push origin feature/NewFeature
```
5. Open a Pull Request.
