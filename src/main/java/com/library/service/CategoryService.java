package com.library.service;

import com.library.dto.CategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);
    CategoryDTO getCategory(Long id);
    void deleteCategory(Long id);
    Page<CategoryDTO> getAllCategories(Pageable pageable);
    List<CategoryDTO> getRootCategories();
    List<CategoryDTO> getSubcategories(Long parentId);
    Page<CategoryDTO> searchCategories(String query, Pageable pageable);
} 