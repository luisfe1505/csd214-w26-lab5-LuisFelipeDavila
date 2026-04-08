***

# Bookstore CLI - Lecture 7 (Winter 2026)
> **Baseline Codebase for Lecture 7: Services, IoC, & Dependency Injection**

[**Click here for the Lecture 7 Notes**](https://docs.google.com/document/d/1CNWeKsbrgE8uDYgdh7PUsoQvBi3bSh4NAqlRU6gNyZk/edit?usp=sharing)

This repository demonstrates the final stage of "decoupling" in a raw Java application. We move away from tight coupling where the application creates its own dependencies, and instead move to an **Inversion of Control (IoC)** model where dependencies are **Injected**.

## 🎯 Lecture 7 Learning Objectives
* **The Service Layer:** Implementing the "Chef" of the application to handle business rules.
* **Inversion of Control (IoC):** Moving the "Wiring Phase" out of the application logic and into a central assembler (`Main.java`).
* **Dependency Injection (DI):** Using Constructor Injection to pass repositories into services.
* **Plug-and-Play Architecture:** Swapping between different storage strategies at runtime without changing a single line of business logic.

## 🍽 The Restaurant Metaphor
To understand this architecture, we use the **Chef and Waiter** analogy:
*   **The Waiter (`App.java`):** The Presentation Layer. Takes orders (User Input) but doesn't know how to cook.
*   **The Pantry (`IRepository`):** The Data Access Layer. Holds the ingredients (Data) but doesn't know the recipes.
*   **The Chef (`BookstoreService`):** The Business Logic Layer. Receives the order from the Waiter, gets ingredients from the Pantry, applies the recipe (rules), and hands the result back.

## 🛠 Supported Storage Strategies
This codebase allows you to "plug in" different Pantries into the Chef at startup:
1.  **In-Memory (ArrayList):** A volatile storage strategy using standard Java collections.
2.  **H2 Database:** An in-memory SQL engine that runs inside your app (No Docker required!).
3.  **MySQL Database:** Persistent production storage (Requires Docker).

---

## 🚀 How it Works (The IoC Container)
The logic for choosing the repository has been moved to **`Main.java`**. When you start the application, you will see a prompt:

```text
Select Data Persistence Strategy:
1. In-Memory (ArrayList)
2. H2 Database (In-Memory SQL)
3. MySQL Database (Production)
```

Once you choose, `Main.java` instantiates the specific repository class and **injects** it into the `App` and `Service` layers via their constructors. The `App` layer never uses the `new` keyword to create a repository.

---

## 📋 Prerequisites
* **Java JDK:** 24
* **Maven:** 3.9+
* **Docker:** (Optional) Only required if choosing the MySQL strategy.

## 🚦 Getting Started

### 1. (Optional) Start Docker
If you intend to use Option 3 (MySQL):
```bash
docker-compose up -d
```

### 2. Compile and Run
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="csd214.bookstore.Main"
```

---

## 🏗 Project Structure
```text
src/main/java/csd214/bookstore/
├── App.java                 # Presentation Layer (The Waiter)
├── Main.java                # IoC Container (The Assembler/Wiring)
├── entities/                # Database Models
├── pojos/                   # UI Data Transfer Objects
├── services/                # Business Logic Layer (The Chef)
│   └── BookstoreService.java
└── repositories/            # Data Access Layer (The Pantry)
    ├── IRepository.java     # The Contract
    ├── InMemoryListRepository.java
    ├── H2Repository.java
    └── MySqlRepository.java
```

## ⚖️ License
Educational use for CSD214 - Sault College.


## Lab 5 Reflection

### 1. The Power of the Interface
When I swapped the InMemoryListRepository for the InMemoryMapRepository, I did not have to change anything in my App.java or MobileService.java. This is because both repositories use the same IRepository interface. This is very helpful when working in a team because one person can change how the data is stored without breaking the work of others.

### 2. Algorithmic Experience (O(n) vs. O(1))
Implementing the HashMap felt much more efficient. With the ArrayList, I had to use a loop to find an item, which takes more time as the list grows (O(n)). But with the HashMap, the search is instant (O(1)) regardless of how many items there are. The Repository Pattern is great because I can improve the performance "under the hood" and the UI code stays simple.

### 3. Production Stability vs. Development Speed
In a real project, MySqlRepository is better because it is persistent. RAM-based storage is faster for testing, but all data is lost when the app stops. MySQL saves the data on the disk using Docker, so it is stable for a real-world store and the information is never lost.

### 4. The Manual IoC Experience
Moving all the "new" keywords to the Main.java felt a bit strange at first, but it makes the app much easier to manage. Everything is in one central location. I think Spring Boot does this "magic" automatically by looking for annotations like @Service and @Repository to connect the classes so I don't have to do it manually in the Main.

### 5. Single Responsibility & Reusability
Moving my logic to MobileService made my App.java much cleaner. Before, the UI was doing too many things. Now, if I want to make a mobile app or a website for this store in the future, I can just reuse the same MobileService without rewriting the business rules. It is like the "portable brain" of the application.