Library Management System - API and H2 database
1. Running the Application

Prerequisites: Java 23+, Maven.
Steps:
Clone the repo: git clone https://github.com/AyaMakansi/library-management-system.git
Build the app: mvn clean install
Run the app: mvn spring-boot:run
Access the app at http://localhost:8080.
2. API Endpoints

Book Management:

GET /api/books: List all books.
GET /api/books/{id}: Get book by ID.
POST /api/books: Add a new book.
PUT /api/books/{id}: Update book.
DELETE /api/books/{id}: Delete book.
Patron Management:

GET /api/patrons: List all patrons.
GET /api/patrons/{id}: Get patron by ID.
POST /api/patrons: Add a new patron.
PUT /api/patrons/{id}: Update patron.
DELETE /api/patrons/{id}: Remove patron.
Borrowing Operations:

POST /api/borrow/{bookId}/patron/{patronId}: Borrow a book.
PUT /api/return/{bookId}/patron/{patronId}: Return a borrowed book.
