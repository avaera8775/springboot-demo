package com.demo.library.repository;

import com.demo.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for Book.
 *
 * Just by extending JpaRepository<Book, Long> you get:
 *   findAll(), findById(), save(), deleteById(), count(), existsById()
 *   ... and much more — all implemented automatically by Spring Data.
 *
 * Custom queries can be derived from method names:
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Spring Data derives SQL from the method name:
    //   SELECT * FROM books WHERE author = ?
    List<Book> findByAuthor(String author);

    // SELECT * FROM books WHERE title LIKE %keyword%
    List<Book> findByTitleContainingIgnoreCase(String keyword);

    // SELECT * FROM books WHERE published_year = ?
    List<Book> findByPublishedYear(int year);
}
