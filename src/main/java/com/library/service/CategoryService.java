package com.library.service;

import com.library.dto.CategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface CategoryService {
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    Optional<CategoryDTO> updateCategory(Long id, CategoryDTO categoryDTO);
    Optional<CategoryDTO> getCategory(Long id);
    boolean deleteCategory(Long id);
    Page<CategoryDTO> getAllCategories(Pageable pageable);
    List<CategoryDTO> getRootCategories();
    List<CategoryDTO> getSubcategories(Long parentId);
    Page<CategoryDTO> searchCategories(String query, Pageable pageable);
    Page<CategoryDTO> getCategoriesByParent(Long parentId, Pageable pageable);
    Page<CategoryDTO> getRootCategories(Pageable pageable);
} 