package com.library.service.impl;

import com.library.dto.CategoryDTO;
import com.library.model.Book;
import com.library.model.Category;
import com.library.repository.BookRepository;
import com.library.repository.CategoryRepository;
import com.library.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        updateCategoryFromDTO(category, categoryDTO);
        Category savedCategory = categoryRepository.save(category);
        return convertToDTO(savedCategory);
    }

    @Override
    public Optional<CategoryDTO> updateCategory(Long id, CategoryDTO categoryDTO) {
        return categoryRepository.findById(id)
                .map(category -> {
                    updateCategoryFromDTO(category, categoryDTO);
                    return convertToDTO(categoryRepository.save(category));
                });
    }

    @Override
    public Optional<CategoryDTO> getCategory(Long id) {
        return categoryRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    public boolean deleteCategory(Long id) {
        if (categoryRepository.existsById(id)) {
            Category category = categoryRepository.findById(id).get();
            if (!category.getChildren().isEmpty()) {
                throw new IllegalStateException("Cannot delete category with subcategories");
            }
            if (!category.getBooks().isEmpty()) {
                throw new IllegalStateException("Cannot delete category with associated books");
            }
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Page<CategoryDTO> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(this::convertToDTO);
    }

    @Override
    public Page<CategoryDTO> getRootCategories(Pageable pageable) {
        return categoryRepository.findRootCategories(pageable)
                .map(this::convertToDTO);
    }

    @Override
    public Page<CategoryDTO> getCategoriesByParent(Long parentId, Pageable pageable) {
        return categoryRepository.findByParentId(parentId, pageable)
                .map(this::convertToDTO);
    }

    @Override
    public Page<CategoryDTO> searchCategories(String query, Pageable pageable) {
        return categoryRepository.findByNameContainingIgnoreCase(query, pageable)
                .map(this::convertToDTO);
    }

    @Override
    public List<CategoryDTO> getRootCategories() {
        return categoryRepository.findAllRootCategories().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryDTO> getSubcategories(Long parentId) {
        return categoryRepository.findAllByParentId(parentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private void updateCategoryFromDTO(Category category, CategoryDTO dto) {
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());

        if (dto.getParentId() != null) {
            Category parent = categoryRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent category not found with id: " + dto.getParentId()));
            category.setParent(parent);
        } else {
            category.setParent(null);
        }

        if (dto.getBookIds() != null && !dto.getBookIds().isEmpty()) {
            Set<Book> books = new HashSet<>();
            for (Long bookId : dto.getBookIds()) {
                Book book = bookRepository.findById(bookId)
                        .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + bookId));
                books.add(book);
            }
            category.setBooks(books);
        }
    }

    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        
        if (category.getParent() != null) {
            dto.setParentId(category.getParent().getId());
        }
        
        dto.setChildrenIds(category.getChildren().stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));
        
        dto.setBookIds(category.getBooks().stream()
                .map(Book::getId)
                .collect(Collectors.toSet()));
        
        return dto;
    }
} 