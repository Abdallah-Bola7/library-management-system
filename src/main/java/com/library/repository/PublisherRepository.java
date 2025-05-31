package com.library.repository;

import com.library.model.Publisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    Page<Publisher> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    @Query("SELECT p FROM Publisher p WHERE p.id IN :ids")
    List<Publisher> findByIds(@Param("ids") List<Long> ids);
    
    boolean existsByNameIgnoreCase(String name);
    
    @Query("SELECT COUNT(b) FROM Publisher p JOIN p.books b WHERE p.id = :publisherId")
    Long countBooksByPublisher(@Param("publisherId") Long publisherId);
} 