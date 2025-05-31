package com.library.repository;

import com.library.model.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByName(String name);
    
    Page<Author> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    @Query("SELECT a FROM Author a WHERE a.id IN :ids")
    List<Author> findByIds(@Param("ids") List<Long> ids);
    
    @Query("SELECT DISTINCT a FROM Author a JOIN a.books b WHERE b.id = :bookId")
    List<Author> findByBookId(@Param("bookId") Long bookId);
    
    @Query("SELECT COUNT(b) FROM Author a JOIN a.books b WHERE a.id = :authorId")
    Long countBooks(@Param("authorId") Long authorId);
    
    @Query("SELECT a FROM Author a ORDER BY SIZE(a.books) DESC")
    Page<Author> findMostPublishedAuthors(Pageable pageable);
    
    boolean existsByNameIgnoreCase(String name);
} 