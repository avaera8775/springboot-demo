package com.demo.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot entry point.
 *
 * @SpringBootApplication is a convenience annotation that combines:
 *   - @Configuration       → marks this as a source of bean definitions
 *   - @EnableAutoConfiguration → tells Spring Boot to configure beans automatically
 *   - @ComponentScan       → scans for @Component, @Service, @Repository, etc.
 */
@SpringBootApplication
public class LibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
        // After startup, visit:
        //   http://localhost:8080/api/books       → list all books
        //   http://localhost:8080/h2-console       → H2 database browser
        //   http://localhost:8080/actuator/health  → health check
    }
}
