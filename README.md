# Book Catalog API

A comprehensive RESTful API for managing a book catalog, integrated with the Gutenberg Project API for ebook metadata.

## Features

- **CRUD Operations**: Create, read, update, and delete books in the catalog
- **Gutenberg Integration**: Search and import books from Project Gutenberg
- **Advanced Search**: Search by title, author, subject, or full-text search
- **Popular Books**: Filter books by download count
- **Data Persistence**: Spring Data JPA with H2 database
- **Modern Java Features**: Lambdas, Streams, and Method References

## Technologies Used

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **H2 Database** (in-memory)
- **RESTful Web Services**
- **Lambdas & Streams API**
- **Maven**

## API Endpoints

### Book Management
- `GET /api/books` - Get all books
- `GET /api/books/{id}` - Get book by ID
- `POST /api/books` - Add a new book
- `PUT /api/books/{id}` - Update a book
- `DELETE /api/books/{id}` - Delete a book

### Search & Filter
- `GET /api/books/search?query={query}` - Search books
- `GET /api/books/search/author?author={author}` - Search by author
- `GET /api/books/popular?minDownloads={count}` - Get popular books
- `GET /api/books/gutenberg/{gutenbergId}` - Get book by Gutenberg ID

### Gutenberg Integration
- `POST /api/books/import/{gutenbergId}` - Import book from Gutenberg
- `GET /api/books/gutenberg/search` - Search Gutenberg books

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd book-catalog-api
