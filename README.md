# Secure Blog API (with Spring Security & JWT)

This is a complete, backend RESTful API for a blog platform, built from scratch using Spring Boot.

The primary focus of this project was to learn and implement **Spring Security** to handle user authentication and authorization. It includes user registration, password hashing (using `bcrypt`), user login, and JWT (JSON Web Token) generation and validation.

---

## 🚀 Tech Stack

* **Java 17+**
* **Spring Boot**
* **Spring Security** (for authentication and authorization)
* **Java JWT (auth0)** (for creating and validating JWTs)
* **Spring Data JPA** (for database interaction)
* **MySQL** (for permanent, persistent storage)
* **Lombok** (to reduce boilerplate code)
* **Maven** (for project management)

---

## 🏁 How to Run Locally

### Prerequisites

* Java 17+ (JDK)
* IntelliJ IDEA (or any IDE)
* MySQL Server (running locally)
* Postman (for testing the API)

### Setup

1.  **Clone the Repository:**
    ```bash
    git clone [https://github.com/mdsharikansar/secure-blog-api.git](https://github.com/mdsharikansar/secure-blog-api.git)
    cd secure-blog-api
    ```

2.  **Create Your Database:**
    Open MySQL Workbench and run the following command to create the database:
    ```sql
    CREATE DATABASE blog_db;
    ```

3.  **Create `application.properties`:**
    * This project was uploaded *without* the `application.properties` file for security (it's in the `.gitignore`).
    * In IntelliJ, go to `src/main/resources/` and create a new file named `application.properties`.
    * Paste the following code into it, making sure to add your MySQL password:

    ```properties
    # --- MySQL Database Connection ---
    spring.datasource.url=jdbc:mysql://localhost:3000/blog_db
    spring.datasource.username=root
    spring.datasource.password=YOUR_MYSQL_PASSWORD_HERE

    # --- JPA / Hibernate Settings ---
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    
    # --- JWT Secret Key ---
    security.jwt.secret-key=averylongandstrongsecretkeythatisatleast32characterslong
    ```

4.  **Run the Application:**
    Open the project in IntelliJ. It will build automatically. Find and run the `SecureBlogApiApplication.java` file. The server will start on `http://localhost:8080`.

---

## ⚙️ API Endpoints

All endpoints are available under the base URL: `http://localhost:8080`

### Public Endpoints (No Token Required)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Creates a new user. |
| `POST` | `/api/auth/login` | Logs in a user and returns a JWT. |
| `GET` | `/api/posts` | Retrieves a list of all posts. |

### Private Endpoints (Valid Bearer Token Required)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/posts` | Creates a new post (as the logged-in user). |

### Example Bodies

**`POST /api/auth/register`**
```json
{
    "username": "newuser",
    "password": "password123"
}