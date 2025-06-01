package com.library.controller;

import com.library.dto.BookDTO;
import com.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.createBook(bookDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO bookDTO) {
        return bookService.updateBook(id, bookDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBook(@PathVariable Long id) {
        return bookService.getBook(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        return bookService.deleteBook(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<Page<BookDTO>> getAllBooks(
            @RequestParam(required = false) String query,
            Pageable pageable) {
        if (query != null && !query.trim().isEmpty()) {
            return ResponseEntity.ok(bookService.searchBooks(query, pageable));
        }
        return ResponseEntity.ok(bookService.getAllBooks(pageable));
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<Page<BookDTO>> getBooksByAuthor(
            @PathVariable Long authorId,
            Pageable pageable) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(authorId, pageable));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<BookDTO>> getBooksByCategory(
            @PathVariable Long categoryId,
            Pageable pageable) {
        return ResponseEntity.ok(bookService.getBooksByCategory(categoryId, pageable));
    }

    @GetMapping("/publisher/{publisherId}")
    public ResponseEntity<Page<BookDTO>> getBooksByPublisher(
            @PathVariable Long publisherId,
            Pageable pageable) {
        return ResponseEntity.ok(bookService.getBooksByPublisher(publisherId, pageable));
    }

    @GetMapping("/language/{language}")
    public ResponseEntity<Page<BookDTO>> getBooksByLanguage(
            @PathVariable String language,
            Pageable pageable) {
        return ResponseEntity.ok(bookService.getBooksByLanguage(language, pageable));
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<Page<BookDTO>> getBooksByYear(
            @PathVariable Integer year,
            Pageable pageable) {
        return ResponseEntity.ok(bookService.getBooksByYear(year, pageable));
    }

    @GetMapping("/available")
    public ResponseEntity<Page<BookDTO>> getAvailableBooks(Pageable pageable) {
        return ResponseEntity.ok(bookService.getAvailableBooks(pageable));
    }

    @GetMapping("/{id}/available")
    public ResponseEntity<Boolean> isBookAvailable(@PathVariable Long id) {
        return bookService.isBookAvailable(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
} 