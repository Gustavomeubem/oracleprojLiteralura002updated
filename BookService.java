package com.bookcatalog.service;

import com.bookcatalog.dto.BookDTO;
import com.bookcatalog.dto.BookRequest;
import com.bookcatalog.model.Book;
import com.bookcatalog.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {
    
    private final BookRepository bookRepository;
    private final GutenbergService gutenbergService;
    
    public BookService(BookRepository bookRepository, GutenbergService gutenbergService) {
        this.bookRepository = bookRepository;
        this.gutenbergService = gutenbergService;
    }
    
    // Using streams for data transformation
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<BookDTO> getBookById(Long id) {
        return bookRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    public Optional<BookDTO> getBookByGutenbergId(Integer gutenbergId) {
        return bookRepository.findByGutenbergId(gutenbergId)
                .map(this::convertToDTO);
    }
    
    @Transactional
    public BookDTO addBook(BookRequest bookRequest) {
        // Check if book already exists by Gutenberg ID
        bookRepository.findByGutenbergId(bookRequest.getGutenbergId())
                .ifPresent(book -> {
                    throw new IllegalArgumentException("Book with Gutenberg ID " + 
                            bookRequest.getGutenbergId() + " already exists");
                });
        
        Book book = convertToEntity(bookRequest);
        Book savedBook = bookRepository.save(book);
        return convertToDTO(savedBook);
    }
    
    @Transactional
    public Optional<BookDTO> importBookFromGutenberg(Integer gutenbergId) {
        return gutenbergService.getBookFromGutenberg(gutenbergId)
                .map(gutenbergBook -> {
                    // Check if already imported
                    if (bookRepository.findByGutenbergId(gutenbergId).isPresent()) {
                        throw new IllegalArgumentException("Book already imported");
                    }
                    
                    Book savedBook = bookRepository.save(gutenbergBook);
                    return convertToDTO(savedBook);
                });
    }
    
    @Transactional
    public Optional<BookDTO> updateBook(Long id, BookRequest bookRequest) {
        return bookRepository.findById(id)
                .map(existingBook -> {
                    updateEntityFromRequest(existingBook, bookRequest);
                    Book updatedBook = bookRepository.save(existingBook);
                    return convertToDTO(updatedBook);
                });
    }
    
    @Transactional
    public boolean deleteBook(Long id) {
        return bookRepository.findById(id)
                .map(book -> {
                    bookRepository.delete(book);
                    return true;
                })
                .orElse(false);
    }
    
    // Using streams for search functionality
    public List<BookDTO> searchBooks(String query) {
        return bookRepository.searchBooks(query).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<BookDTO> searchBooksByAuthor(String author) {
        return bookRepository.findByAuthorsContainingIgnoreCase(author).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<BookDTO> getPopularBooks(Integer minDownloads) {
        return bookRepository.findPopularBooks(minDownloads != null ? minDownloads : 1000).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<BookDTO> searchGutenbergBooks(String query, String author, String title, String subject) {
        return gutenbergService.searchBooksFromGutenberg(query, author, title, subject).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Helper methods using method references
    private BookDTO convertToDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthors(book.getAuthors());
        dto.setSubjects(book.getSubjects());
        dto.setBookshelves(book.getBookshelves());
        dto.setGutenbergId(book.getGutenbergId());
        dto.setLanguage(book.getLanguage());
        dto.setDownloadCount(book.getDownloadCount());
        dto.setMediaType(book.getMediaType());
        dto.setCoverUrl(book.getCoverUrl());
        dto.setCreatedAt(book.getCreatedAt());
        return dto;
    }
    
    private Book convertToEntity(BookRequest request) {
        Book book = new Book();
        updateEntityFromRequest(book, request);
        return book;
    }
    
    private void updateEntityFromRequest(Book book, BookRequest request) {
        book.setTitle(request.getTitle());
        book.setAuthors(request.getAuthors());
        book.setSubjects(request.getSubjects());
        book.setBookshelves(request.getBookshelves());
        book.setGutenbergId(request.getGutenbergId());
        book.setLanguage(request.getLanguage());
        book.setDownloadCount(request.getDownloadCount());
        book.setMediaType(request.getMediaType());
        book.setCoverUrl(request.getCoverUrl());
    }
}
