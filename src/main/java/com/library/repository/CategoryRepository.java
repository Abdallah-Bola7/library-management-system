package com.library.repository;

import com.library.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    
    List<Category> findByParentIsNull();
    
    @Query("SELECT c FROM Category c WHERE c.parent.id = :parentId")
    List<Category> findByParentId(@Param("parentId") Long parentId);
    
    @Query("SELECT c FROM Category c WHERE c.id IN :ids")
    List<Category> findByIds(@Param("ids") List<Long> ids);
    
    Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    
    boolean existsByNameIgnoreCase(String name);
    
    @Query("SELECT COUNT(b) FROM Category c JOIN c.books b WHERE c.id = :categoryId")
    Long countBooks(@Param("categoryId") Long categoryId);
    
    Boolean existsByNameAndParentId(String name, Long parentId);
    
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.children WHERE c.id = :id")
    Optional<Category> findByIdWithChildren(@Param("id") Long id);
    
    @Query("SELECT c FROM Category c WHERE c.parent IS NULL")
    Page<Category> findRootCategories(Pageable pageable);
    
    @Query("SELECT c FROM Category c WHERE c.parent.id = :parentId")
    Page<Category> findByParentId(@Param("parentId") Long parentId, Pageable pageable);
    
    @Query("SELECT c FROM Category c WHERE " +
           "LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Category> searchCategories(@Param("query") String query, Pageable pageable);
    
    @Query("SELECT DISTINCT c FROM Category c LEFT JOIN FETCH c.parent WHERE c.parent IS NULL")
    List<Category> findAllRootCategories();
    
    @Query("SELECT DISTINCT c FROM Category c LEFT JOIN FETCH c.parent WHERE c.parent.id = :parentId")
    List<Category> findAllByParentId(@Param("parentId") Long parentId);
} 