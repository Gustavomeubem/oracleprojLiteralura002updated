package com.bookcatalog.controller;

import com.bookcatalog.dto.BookDTO;
import com.bookcatalog.dto.BookRequest;
import com.bookcatalog.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
public class BookController {
    
    private final BookService bookService;
    
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        Optional<BookDTO> book = bookService.getBookById(id);
        return book.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/gutenberg/{gutenbergId}")
    public ResponseEntity<BookDTO> getBookByGutenbergId(@PathVariable Integer gutenbergId) {
        Optional<BookDTO> book = bookService.getBookByGutenbergId(gutenbergId);
        return book.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<BookDTO> addBook(@Valid @RequestBody BookRequest bookRequest) {
        try {
            BookDTO savedBook = bookService.addBook(bookRequest);
            return ResponseEntity.ok(savedBook);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/import/{gutenbergId}")
    public ResponseEntity<BookDTO> importBook(@PathVariable Integer gutenbergId) {
        try {
            Optional<BookDTO> importedBook = bookService.importBookFromGutenberg(gutenbergId);
            return importedBook.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, 
                                            @Valid @RequestBody BookRequest bookRequest) {
        Optional<BookDTO> updatedBook = bookService.updateBook(id, bookRequest);
        return updatedBook.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        boolean deleted = bookService.deleteBook(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<BookDTO>> searchBooks(@RequestParam String query) {
        return ResponseEntity.ok(bookService.searchBooks(query));
    }
    
    @GetMapping("/search/author")
    public ResponseEntity<List<BookDTO>> searchBooksByAuthor(@RequestParam String author) {
        return ResponseEntity.ok(bookService.searchBooksByAuthor(author));
    }
    
    @GetMapping("/popular")
    public ResponseEntity<List<BookDTO>> getPopularBooks(
            @RequestParam(required = false) Integer minDownloads) {
        return ResponseEntity.ok(bookService.getPopularBooks(minDownloads));
    }
    
    @GetMapping("/gutenberg/search")
    public ResponseEntity<List<BookDTO>> searchGutenbergBooks(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String subject) {
        return ResponseEntity.ok(bookService.searchGutenbergBooks(query, author, title, subject));
    }
}
