# ⚙️ RFIND

<p align="center">
  <img src="https://img.shields.io/badge/Java-17+-orange?style=for-the-badge&logo=java" alt="Java">
  <img src="https://img.shields.io/badge/Spring_Boot-3.x-green?style=for-the-badge&logo=springboot" alt="Spring Boot">
  <img src="https://img.shields.io/badge/MySQL-00758F?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL">
  <img src="https://img.shields.io/badge/Swagger-OpenAPI-85EA2D?style=for-the-badge&logo=swagger" alt="Swagger">
</p>

---

### 📝 Description
A robust **REST API** for user management, built with **Spring Boot 3**. This project features seamless integration with **Neon (PostgreSQL)** in the cloud, focusing on high security standards, industry-standard encryption, and automated interactive documentation.

---

### 🛠️ Tech Stack

* **Backend:** Java 17+, Spring Boot 3.x
* **Data:** Spring Data JPA, MySQL
* **Security:** Spring Security (Authentication & BCrypt Encryption)
* **Documentation:** SpringDoc OpenAPI (Swagger)
* **Build Tool:** Maven

---

### 📦 Installation & Setup

1️⃣ **Clone the repository**
```bash
git clone [https://github.com/your-username/RFIND-.git](https://github.com/your-username/RFIND-.git)
```

2️⃣ **Configure Database**
Update your `src/main/resources/application.properties` with your Neon credentials:
```properties
spring.datasource.url=jdbc:mysql://your-mysql-host/mysql
spring.datasource.username=your-username
spring.datasource.password=your-password
```

3️⃣ **Run the application**
```bash
mvn clean install
mvn spring-boot:run
```

### 📖 API Documentation

Once the application is running, you can access the interactive documentation and test the endpoints directly in your browser:

👉 http://localhost:8080/swagger-ui/index.html

### 🛡️ Security & Best Practices

- **Zero Plain Text:** Passwords are never stored in plain text; BCrypt hashing is used throughout.

- **Open Access Docs:** Swagger endpoints are explicitly permitted in the Security configuration to ensure accessibility.
