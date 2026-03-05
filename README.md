<p align="center">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/JavaFX-000000?style=for-the-badge&logo=java&logoColor=white" />
  <img src="https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white" />
  <img src="https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge" />
</p>

<h1 align="center"> MovieScope</h1>

<p align="center">
  <strong>A Multi-Factor Personalized Recommendation & Movie Tracker System</strong>
  <br />
  <em>Combining data-driven discovery with high-performance Java engineering</em>
</p>

---

##  Overview
**MovieScope** is a Java-based desktop software project designed to demonstrate high-performance data structures and personalized recommendation algorithms. It features a **Layered Architecture (MVC)** and a functional **JavaFX GUI** for managing viewing histories and generating data-driven suggestions.

##  Key Technical Highlights

###  Multi-Factor Recommendation Engine
The heart of MovieScope is a sophisticated weighted scoring algorithm that goes beyond simple rating-based suggestions:
- **Genre Affinity**: Dynamically weights user preferences based on history (x2) and watchlist (x1).
- **Recency Bonus**: Rewards newer releases to keep content fresh.
- **Exploration Mechanism**: An explicit **Exploration Bonus** to mitigate the cold-start problem for new users, promoting content diversity.

###  High-Efficiency Data Engineering
- **Constant Time Retrieval**: Leverages **HashMap** structures for movie and user data, ensuring **O(1) time complexity** for authentication and lookups.
- **Persistence**: Optimized CSV I/O for lightweight, localized data storage without the overhead of a full SQL database.

###  Robust Software Design
- **OOP Principles**: Strict adherence to encapsulation and dependency injection for a modular, maintainable codebase.
- **Quality Assurance**: Integrated **JUnit 5** for automated unit and integration testing, ensuring 100% logic reliability.

##  Tech Stack
- **Language:** Java 17+
- **GUI Framework:** JavaFX
- **Testing:** JUnit 5
- **Data Format:** CSV
- **IDE:** IntelliJ IDEA / VS Code

##  Getting Started
1. **Clone the repo:**
   ```bash
   git clone https://github.com/LingchenE1/MovieScope.git
