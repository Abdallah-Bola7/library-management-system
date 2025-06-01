package com.library.controller;

import com.library.dto.AuthorDTO;
import com.library.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authors")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @PostMapping
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<AuthorDTO> createAuthor(@Valid @RequestBody AuthorDTO authorDTO) {
        return ResponseEntity.ok(authorService.createAuthor(authorDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable Long id, @Valid @RequestBody AuthorDTO authorDTO) {
        return authorService.updateAuthor(id, authorDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthor(@PathVariable Long id) {
        return authorService.getAuthor(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        return authorService.deleteAuthor(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<Page<AuthorDTO>> getAllAuthors(
            @RequestParam(required = false) String query,
            Pageable pageable) {
        if (query != null && !query.trim().isEmpty()) {
            return ResponseEntity.ok(authorService.searchAuthors(query, pageable));
        }
        return ResponseEntity.ok(authorService.getAllAuthors(pageable));
    }

    @GetMapping("/nationality/{nationality}")
    public ResponseEntity<Page<AuthorDTO>> getAuthorsByNationality(
            @PathVariable String nationality,
            Pageable pageable) {
        return ResponseEntity.ok(authorService.getAuthorsByNationality(nationality, pageable));
    }
} 