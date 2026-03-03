# MovieScope-Multi-Factor-Weighted-Recommendation-System
MovieScope is a high-performance Java desktop application designed to bridge the gap between movie management and personalized discovery. Built on a Layered Architecture (MVC) and powered by JavaFX, the system provides users with an intuitive interface to track their viewing journey and receive data-driven suggestions.

Key Technical Highlights:

Multi-Factor Recommendation Engine: Implements a sophisticated weighted scoring algorithm that integrates user genre preferences (weighted x2), watchlist commitment, content recency, and an explicit Exploration Bonus to mitigate cold-start issues.

High-Efficiency Data Engineering: Leverages HashMap structures for movie and user data management, ensuring O(1) time complexity for authentication and data retrieval.

Robust Software Design: Adheres to Object-Oriented Programming (OOP) principles (Encapsulation, Dependency Injection) and utilizes JUnit 5 for automated unit and integration testing to ensure system stability.

End-to-End Functionality: Supports secure user authentication, multi-modal list management (Watchlist/History), and persistent data storage via localized CSV I/O.
