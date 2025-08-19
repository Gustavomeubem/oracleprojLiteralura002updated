package com.bookcatalog.repository;

import com.bookcatalog.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    // Using Spring Data JPA query methods with lambdas
    Optional<Book> findByGutenbergId(Integer gutenbergId);
    
    List<Book> findByTitleContainingIgnoreCase(String title);
    
    List<Book> findByAuthorsContainingIgnoreCase(String author);
    
    List<Book> findBySubjectsContainingIgnoreCase(String subject);
    
    List<Book> findByLanguage(String language);
    
    List<Book> findByDownloadCountGreaterThanEqual(Integer minDownloads);
    
    // Custom query using JPQL with streams processing
    @Query("SELECT b FROM Book b WHERE b.downloadCount >= :minDownloads ORDER BY b.downloadCount DESC")
    List<Book> findPopularBooks(@Param("minDownloads") Integer minDownloads);
    
    // Custom query for search across multiple fields
    @Query("SELECT b FROM Book b WHERE " +
           "LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "EXISTS (SELECT a FROM b.authors a WHERE LOWER(a) LIKE LOWER(CONCAT('%', :query, '%'))) OR " +
           "EXISTS (SELECT s FROM b.subjects s WHERE LOWER(s) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Book> searchBooks(@Param("query") String query);
}
