package com.demo.library.exception;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * BookNotFoundException — thrown when a book ID doesn't exist.
 * The @ResponseStatus annotation tells Spring to return 404 automatically.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(Long id) {
        super("Book not found with id: " + id);
    }
}

/**
 * GlobalExceptionHandler — handles errors across all controllers.
 *
 * @RestControllerAdvice combines @ControllerAdvice + @ResponseBody,
 * so every @ExceptionHandler method returns JSON automatically.
 */
@RestControllerAdvice
class GlobalExceptionHandler {

    /**
     * Handles validation errors from @Valid (e.g. blank title, invalid year).
     * Returns a 400 Bad Request with a map of field → error message.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
          .forEach(err -> fieldErrors.put(err.getField(), err.getDefaultMessage()));

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", 400);
        body.put("error", "Validation Failed");
        body.put("fields", fieldErrors);

        return ResponseEntity.badRequest().body(body);
    }

    /**
     * Handles our custom BookNotFoundException → 404 Not Found.
     */
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(BookNotFoundException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", 404);
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}

// Export so controller can use it
class Exceptions {
    public static BookNotFoundException bookNotFound(Long id) {
        return new BookNotFoundException(id);
    }
}
