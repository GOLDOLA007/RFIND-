# RFIND | Business Tracking and Management System

<p align="center">
  <img src="https://img.shields.io/badge/Java-17+-orange?style=for-the-badge&logo=java" alt="Java">
  <img src="https://img.shields.io/badge/Spring_Boot-3.x-green?style=for-the-badge&logo=springboot" alt="Spring Boot">
  <img src="https://img.shields.io/badge/MySQL-00758F?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL">
  <img src="https://img.shields.io/badge/Swagger-OpenAPI-85EA2D?style=for-the-badge&logo=swagger" alt="Swagger">
</p>

---

### 📝 Description
**RFIND** is a complete internal monitoring and management solution, designed to integrate administrative control of assets and personnel with the precision of embedded systems. The system allows not only the management of basic records, but also real-time tracking of employee movement via sensors.

---

### ✨ Key Features

- **Full CRUD Management:** Products, Employees, and Locations.
- **Hardware Integration:** Real-time logging of employee movement via Arduino sensors.
- **Smart Tracking:** Logs include Employee ID, Timestamp, Location, and Sensor ID.
- **Automated Reporting:** Generate professional PDF reports of all access logs.
- **Modern Security:** Role-based access and password encryption.

### 🛠️ Tech Stack

* **Backend:** Java 17+, Spring Boot 3.x
* **Data:** Spring Data JPA, MySQL
* **Frontend:** Java Swing
* **Hardware:** Arduino (Integration of sensors for data capture).
* **Reports:** JasperReports / iText (PDF document generation).
* **Security:** Spring Security (Authentication & BCrypt Encryption)
* **Documentation:** SpringDoc OpenAPI (Swagger)
* **Build Tool:** Maven

---

### 🔌 Arduino Setup

The hardware component uses an **Arduino [Uno]** with **[RFID]**. 
The communication with the Spring Boot backend is handled via **[Serial Port]**, ensuring that every physical detection triggers a digital record in the database.

---

### 📦 Installation & Setup

1️⃣ **Clone the repository**
```bash
git clone [https://github.com/your-username/RFIND-.git](https://github.com/your-username/RFIND-.git)
```

2️⃣ **Configure Database**
Update your `src/main/resources/application.properties` with your database credentials:
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
