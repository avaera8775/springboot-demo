package com.demo.library.controller;

import com.demo.library.model.Book;
import com.demo.library.repository.BookRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * BookController — exposes the Book Library REST API.
 *
 * @RestController = @Controller + @ResponseBody
 *   → every method return value is serialized to JSON automatically.
 *
 * @RequestMapping("/api/books") sets the base URL for all endpoints.
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    // Spring injects the repository automatically via @Autowired
    @Autowired
    private BookRepository bookRepository;

    // ── GET /api/books ────────────────────────────────────────────────────────
    // Returns all books. Optionally filter by author or keyword.
    //
    // Examples:
    //   GET /api/books
    //   GET /api/books?author=Tolkien
    //   GET /api/books?keyword=lord
    @GetMapping
    public List<Book> getAllBooks(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String keyword) {

        if (author != null) {
            return bookRepository.findByAuthor(author);
        }
        if (keyword != null) {
            return bookRepository.findByTitleContainingIgnoreCase(keyword);
        }
        return bookRepository.findAll();
    }

    // ── GET /api/books/{id} ───────────────────────────────────────────────────
    // Returns a single book by ID, or 404 if not found.
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found: " + id));
        return ResponseEntity.ok(book);
    }

    // ── POST /api/books ───────────────────────────────────────────────────────
    // Creates a new book. Returns 201 Created with a Location header.
    //
    // @Valid triggers bean validation (@NotBlank, @Size, etc.) on the Book object.
    // If validation fails, Spring returns 400 Bad Request automatically.
    @PostMapping
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        Book saved = bookRepository.save(book);

        // Build the URI for the newly created resource
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    // ── PUT /api/books/{id} ───────────────────────────────────────────────────
    // Updates an existing book. Returns 200 OK with the updated book.
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id,
                                           @Valid @RequestBody Book updated) {
        Book existing = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found: " + id));

        existing.setTitle(updated.getTitle());
        existing.setAuthor(updated.getAuthor());
        existing.setPublishedYear(updated.getPublishedYear());
        existing.setDescription(updated.getDescription());

        return ResponseEntity.ok(bookRepository.save(existing));
    }

    // ── DELETE /api/books/{id} ────────────────────────────────────────────────
    // Deletes a book. Returns 204 No Content on success.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found: " + id);
        }
        bookRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
