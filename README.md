# Library Management System

A robust and scalable Library Management System built with Spring Boot, providing comprehensive features for managing books, users, and lending operations.

## Features

### Core Functionality
- Book management with extensive metadata
- Multiple authors per book support
- Hierarchical category system
- Publisher management
- Member management
- Borrowing system with due date tracking
- Overdue book notifications

### Technical Features
- Role-based access control (ADMIN, LIBRARIAN, STAFF)
- JWT-based authentication
- RESTful API architecture
- Pagination and sorting support
- Advanced search capabilities
- MySQL database

## Technology Stack

- **Backend Framework:** Spring Boot
- **Security:** Spring Security with JWT
- **Database:** MySQL 8.0
- **ORM:** Spring Data JPA
- **API Documentation:** Postman Collection
- **Build Tool:** Maven

## Getting Started

### Prerequisites
- JDK 17 or higher
- Maven
- MySQL 8.0 or higher

### Installation

1. Clone the repository:
```bash
git clone [repository-url]
```

2. Create MySQL database:
```sql
CREATE DATABASE library_db;
```

3. Configure the database in `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/library_db?useSSL=false&serverTimezone=UTC
    username: your_username
    password: your_password
  jpa:
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
    hibernate:
      ddl-auto: update
```

4. Build the project:
```bash
mvn clean install
```

5. Run the application:
```bash
mvn spring-boot:run
```

## API Testing with Postman

1. Import the provided `Library_Management_System.postman_collection.json` into Postman
2. Set up environment variables in Postman:
   - `base_url`: http://localhost:8080
   - `jwt_token`: (You'll get this after login)
3. Use the collection's endpoints in this order:
   - Use "Login" API to get JWT token
   - Set the received token as `jwt_token` environment variable
   - Now you can use all other endpoints

## API Endpoints

### Authentication
- POST `/api/auth/login` - User login
- POST `/api/auth/signup` - User registration

### Books
- GET `/api/books` - List all books
  - Optional query parameters:
    - `query` - Search term for book titles (if provided, returns books matching the search term)
- GET `/api/books/{id}` - Get book details
- POST `/api/books` - Add new book
- PUT `/api/books/{id}` - Update book
- DELETE `/api/books/{id}` - Delete book
- GET `/api/books/author/{authorId}` - Get books by author
- GET `/api/books/category/{categoryId}` - Get books by category
- GET `/api/books/available` - Get available books
- GET `/api/books/{id}/available` - Check if a specific book is available

### Authors
- GET `/api/authors` - List all authors
- GET `/api/authors/{id}` - Get author details
- POST `/api/authors` - Add new author
- PUT `/api/authors/{id}` - Update author
- DELETE `/api/authors/{id}` - Delete author

### Borrowing
- POST `/api/borrow/checkout` - Checkout books
- POST `/api/borrow/return` - Return books
- GET `/api/borrow/user/{userId}` - Get user's borrowed books
- GET `/api/borrow/overdue` - Get overdue books

## Database Schema

### Core Entities
- User (id, username, password, role, etc.)
- Book (id, title, ISBN, publication_date, etc.)
- Author (id, name, biography, etc.)
- Category (id, name, parent_id, etc.)
- Publisher (id, name, contact_info, etc.)
- Member (id, name, contact_info, membership_status, etc.)
- BorrowRecord (id, book_id, member_id, borrow_date, due_date, return_date, etc.)

## Security

The application implements a comprehensive security model:
- JWT-based authentication
- Role-based access control
- Password encryption
- Secure endpoints with proper authorization

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details 