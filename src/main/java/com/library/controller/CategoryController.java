package com.library.controller;

import com.library.dto.CategoryDTO;
import com.library.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.createCategory(categoryDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO) {
        return categoryService.updateCategory(id, categoryDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable Long id) {
        return categoryService.getCategory(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        return categoryService.deleteCategory(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<Page<CategoryDTO>> getAllCategories(
            @RequestParam(required = false) String query,
            Pageable pageable) {
        if (query != null && !query.trim().isEmpty()) {
            return ResponseEntity.ok(categoryService.searchCategories(query, pageable));
        }
        return ResponseEntity.ok(categoryService.getAllCategories(pageable));
    }

    @GetMapping("/root")
    public ResponseEntity<Page<CategoryDTO>> getRootCategories(Pageable pageable) {
        return ResponseEntity.ok(categoryService.getRootCategories(pageable));
    }

    @GetMapping("/root/all")
    public ResponseEntity<List<CategoryDTO>> getAllRootCategories() {
        return ResponseEntity.ok(categoryService.getRootCategories());
    }

    @GetMapping("/parent/{parentId}")
    public ResponseEntity<Page<CategoryDTO>> getCategoriesByParent(
            @PathVariable Long parentId,
            Pageable pageable) {
        return ResponseEntity.ok(categoryService.getCategoriesByParent(parentId, pageable));
    }

    @GetMapping("/parent/{parentId}/all")
    public ResponseEntity<List<CategoryDTO>> getAllSubcategories(@PathVariable Long parentId) {
        return ResponseEntity.ok(categoryService.getSubcategories(parentId));
    }
} 