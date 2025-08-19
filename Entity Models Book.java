package com.bookcatalog.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is mandatory")
    private String title;

    @ElementCollection
    private List<String> authors;

    private List<String> subjects;
    private List<String> bookshelves;
    
    @NotNull(message = "Gutenberg ID is mandatory")
    private Integer gutenbergId;
    
    private String language;
    private Integer downloadCount;
    private String mediaType;
    
    @Column(length = 1000)
    private String coverUrl;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constructors
    public Book() {}

    public Book(String title, List<String> authors, Integer gutenbergId) {
        this.title = title;
        this.authors = authors;
        this.gutenbergId = gutenbergId;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public List<String> getAuthors() { return authors; }
    public void setAuthors(List<String> authors) { this.authors = authors; }

    public List<String> getSubjects() { return subjects; }
    public void setSubjects(List<String> subjects) { this.subjects = subjects; }

    public List<String> getBookshelves() { return bookshelves; }
    public void setBookshelves(List<String> bookshelves) { this.bookshelves = bookshelves; }

    public Integer getGutenbergId() { return gutenbergId; }
    public void setGutenbergId(Integer gutenbergId) { this.gutenbergId = gutenbergId; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public Integer getDownloadCount() { return downloadCount; }
    public void setDownloadCount(Integer downloadCount) { this.downloadCount = downloadCount; }

    public String getMediaType() { return mediaType; }
    public void setMediaType(String mediaType) { this.mediaType = mediaType; }

    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
