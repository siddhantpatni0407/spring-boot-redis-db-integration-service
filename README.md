# 📌 spring-boot-redis-db-integration-service

## 🚀 Overview

Spring Boot application for **Employee Management** using **Redis as the database**.

* This service manages all employee data in **Redis**.
* The following **CRUD endpoints** are available:

    1. Add Employee
    2. Get All Employees
    3. Get Employee by ID
    4. Update Employee
    5. Delete Employee

---

## ✅ Tech Stack

* **Java 21**
* **Spring Boot 3.x**
* **Spring Data Redis**
* **Lombok**
* **Redis Server**

---

## ⚙️ Setup & Run

### 1. Install Redis on Windows

1. Download Redis for Windows from:
   **[Redis Windows Download (Microsoft Archive)](https://github.com/microsoftarchive/redis/releases/tag/win-3.2.100)**

2. Extract or install Redis to a folder, for example:

   ```
   C:\Program Files\Redis
   ```

3. Add Redis folder path to **System Environment Variables**:

   ```
   C:\Program Files\Redis
   ```

---

### 2. Start Redis Server

1. Open **Command Prompt as Administrator**.
2. Navigate to the Redis directory:

   ```bash
   cd "C:\Program Files\Redis"
   ```
3. Start the Redis server:

   ```bash
   redis-server.exe redis.windows.conf
   ```

   ✅ You should see `Ready to accept connections`.

---

### 3. Test Redis Connection

Run:

```bash
redis-cli
```

Then type:

```bash
ping
```

Expected response:

```
PONG
```

---

### 4. Clone the Project

```bash
git clone https://github.com/your-repo/spring-boot-employee-redis-service.git
cd spring-boot-employee-redis-service
```

---

### 5. Run the Application

```bash
mvn clean install
mvn spring-boot:run
```

The service will start at:
**`http://localhost:8080/api/v1/employee-mgmt-service`**

---

## 🏗 Architecture Overview

![employee-redis-architecture.png](src/main/resources/artifacts/employee-redis-architecture.png)

---

## 🔄 Sequence Diagram

![employee-redis-sequence-diagram.png](src/main/resources/artifacts/employee-redis-sequence-diagram.png)

---

## 📌 API Details

### **Base URL**

```
http://localhost:8080/api/v1/employee-mgmt-service
```

---

### ✅ 1. Add Employee

* **URL:** `/employee`
* **Method:** `POST`
* **Request:**

```json
{
  "id": "101",
  "name": "Siddhant",
  "department": "Engineering",
  "salary": 100000
}
```

* **Response:**

```json
{
  "id": "101",
  "name": "Siddhant",
  "department": "Engineering",
  "salary": 100000.0
}
```

---

### ✅ 2. Get All Employees

* **URL:** `/employee`
* **Method:** `GET`
* **Response:**

```json
[
  {
    "id": "101",
    "name": "Siddhant",
    "department": "Engineering",
    "salary": 100000.0
  }
]
```

---

### ✅ 3. Get Employee by ID

* **URL:** `/employee/{id}`
* **Method:** `GET`
* **Response:**

```json
{
  "id": "101",
  "name": "Siddhant",
  "department": "Engineering",
  "salary": 100000.0
}
```

---

### ✅ 4. Update Employee

* **URL:** `/employee/{id}`
* **Method:** `PUT`
* **Request:**

```json
{
  "name": "Siddhant Patni",
  "department": "Technology",
  "salary": 120000
}
```

* **Response:**

```json
{
  "id": "101",
  "name": "Siddhant Patni",
  "department": "Technology",
  "salary": 120000.0
}
```

---

### ✅ 5. Delete Employee

* **URL:** `/employee/{id}`
* **Method:** `DELETE`
* **Response:**

```
Employee deleted successfully
```

---

## ✅ Data Store

* **Redis Key:** `Employee`
* **Serialization:** `Serializable` via `@RedisHash("Employee")`

---

### 🔗 Future Enhancements:

* Implement **Pagination & Sorting**
* Add **Spring Security** for Authentication
* Integrate with **Kafka for Event Streaming**

---