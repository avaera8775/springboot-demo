package com.demo.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * JPA Entity — maps directly to the "books" table in H2.
 * Spring Data + Hibernate create the table automatically at startup.
 */
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must be under 200 characters")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Author is required")
    @Column(nullable = false)
    private String author;

    @Min(value = 1000, message = "Year must be a valid year")
    @Max(value = 2100, message = "Year must be a valid year")
    private int publishedYear;

    @Size(max = 1000)
    private String description;

    // ── Constructors ──────────────────────────────────────────────────────────

    public Book() {}

    public Book(String title, String author, int publishedYear, String description) {
        this.title = title;
        this.author = author;
        this.publishedYear = publishedYear;
        this.description = description;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public int getPublishedYear() { return publishedYear; }
    public void setPublishedYear(int publishedYear) { this.publishedYear = publishedYear; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
