package com.bookcatalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class BookRequest {
    @NotBlank(message = "Title is mandatory")
    private String title;

    private List<String> authors;
    private List<String> subjects;
    private List<String> bookshelves;

    @NotNull(message = "Gutenberg ID is mandatory")
    private Integer gutenbergId;
    
    private String language;
    private Integer downloadCount;
    private String mediaType;
    private String coverUrl;

    // Getters and Setters
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
}
