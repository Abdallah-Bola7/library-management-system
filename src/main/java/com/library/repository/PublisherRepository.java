package com.library.repository;

import com.library.model.Publisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    Optional<Publisher> findByName(String name);
    
    Page<Publisher> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    Page<Publisher> findByCountry(String country, Pageable pageable);
    
    @Query("SELECT p FROM Publisher p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Publisher> searchPublishers(@Param("query") String query, Pageable pageable);
    
    @Query("SELECT p FROM Publisher p ORDER BY SIZE(p.books) DESC")
    Page<Publisher> findMostActivePublishers(Pageable pageable);
    
    @Query("SELECT COUNT(b) FROM Publisher p JOIN p.books b WHERE p.id = :publisherId")
    Long countBooks(@Param("publisherId") Long publisherId);
    
    @Query("SELECT DISTINCT p.country FROM Publisher p WHERE p.country IS NOT NULL")
    List<String> findAllCountries();
    
    boolean existsByNameIgnoreCase(String name);
} 