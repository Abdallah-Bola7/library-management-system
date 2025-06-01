package com.library.controller;

import com.library.dto.PublisherDTO;
import com.library.service.PublisherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/publishers")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PublisherController {

    @Autowired
    private PublisherService publisherService;

    @PostMapping
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<PublisherDTO> createPublisher(@Valid @RequestBody PublisherDTO publisherDTO) {
        return ResponseEntity.ok(publisherService.createPublisher(publisherDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<PublisherDTO> updatePublisher(@PathVariable Long id, @Valid @RequestBody PublisherDTO publisherDTO) {
        return publisherService.updatePublisher(id, publisherDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherDTO> getPublisher(@PathVariable Long id) {
        return publisherService.getPublisher(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePublisher(@PathVariable Long id) {
        return publisherService.deletePublisher(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<Page<PublisherDTO>> getAllPublishers(
            @RequestParam(required = false) String query,
            Pageable pageable) {
        if (query != null && !query.trim().isEmpty()) {
            return ResponseEntity.ok(publisherService.searchPublishers(query, pageable));
        }
        return ResponseEntity.ok(publisherService.getAllPublishers(pageable));
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<Page<PublisherDTO>> getPublishersByCountry(
            @PathVariable String country,
            Pageable pageable) {
        return ResponseEntity.ok(publisherService.getPublishersByCountry(country, pageable));
    }

    @GetMapping("/most-active")
    public ResponseEntity<Page<PublisherDTO>> getMostActivePublishers(Pageable pageable) {
        return ResponseEntity.ok(publisherService.getMostActivePublishers(pageable));
    }
} 