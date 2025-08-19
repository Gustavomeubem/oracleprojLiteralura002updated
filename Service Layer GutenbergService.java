package com.bookcatalog.service;

import com.bookcatalog.model.Book;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GutenbergService {
    
    private static final String GUTENDEX_BASE_URL = "https://gutendex.com/books";
    private final RestTemplate restTemplate;
    
    public GutenbergService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    // Using streams and lambdas to process API responses
    public List<Book> searchBooksFromGutenberg(String query, String author, String title, String subject) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(GUTENDEX_BASE_URL);
        
        Map<String, String> params = new HashMap<>();
        if (query != null && !query.trim().isEmpty()) params.put("search", query);
        if (author != null && !author.trim().isEmpty()) params.put("author", author);
        if (title != null && !title.trim().isEmpty()) params.put("title", title);
        if (subject != null && !subject.trim().isEmpty()) params.put("topic", subject);
        
        params.forEach(builder::queryParam);
        
        try {
            Map<String, Object> response = restTemplate.getForObject(builder.toUriString(), Map.class);
            
            if (response != null && response.containsKey("results")) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
                
                return results.stream()
                        .map(this::mapToBook)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            // Log the error and return empty list
            System.err.println("Error fetching from Gutenberg API: " + e.getMessage());
        }
        
        return Collections.emptyList();
    }
    
    public Optional<Book> getBookFromGutenberg(Integer gutenbergId) {
        String url = GUTENDEX_BASE_URL + "/" + gutenbergId;
        
        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            return Optional.ofNullable(mapToBook(response));
        } catch (Exception e) {
            System.err.println("Error fetching book from Gutenberg API: " + e.getMessage());
            return Optional.empty();
        }
    }
    
    @SuppressWarnings("unchecked")
    private Book mapToBook(Map<String, Object> bookData) {
        if (bookData == null) return null;
        
        Book book = new Book();
        book.setGutenbergId((Integer) bookData.get("id"));
        book.setTitle((String) bookData.get("title"));
        
        // Process authors using streams
        List<Map<String, Object>> authors = (List<Map<String, Object>>) bookData.get("authors");
        if (authors != null) {
            List<String> authorNames = authors.stream()
                    .map(author -> (String) author.get("name"))
                    .collect(Collectors.toList());
            book.setAuthors(authorNames);
        }
        
        // Process subjects
        List<String> subjects = (List<String>) bookData.get("subjects");
        book.setSubjects(subjects);
        
        // Process bookshelves
        List<String> bookshelves = (List<String>) bookData.get("bookshelves");
        book.setBookshelves(bookshelves);
        
        book.setLanguage((String) bookData.get("languages") != null ? 
            ((List<String>) bookData.get("languages")).get(0) : null);
        book.setDownloadCount((Integer) bookData.get("download_count"));
        
        // Get cover image URL
        Map<String, String> formats = (Map<String, String>) bookData.get("formats");
        if (formats != null) {
            book.setCoverUrl(formats.get("image/jpeg"));
        }
        
        return book;
    }
}
