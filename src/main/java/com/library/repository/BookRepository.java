package com.library.repository;

import com.library.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);
    
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    
    @Query("SELECT b FROM Book b WHERE b.availableCopies > 0")
    Page<Book> findAvailableBooks(Pageable pageable);
    
    @Query("SELECT b FROM Book b JOIN b.authors a WHERE a.id = :authorId")
    Page<Book> findByAuthorId(@Param("authorId") Long authorId, Pageable pageable);
    
    @Query("SELECT b FROM Book b JOIN b.categories c WHERE c.id = :categoryId")
    Page<Book> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);
    
    @Query("SELECT b FROM Book b WHERE b.publisher.id = :publisherId")
    Page<Book> findByPublisherId(@Param("publisherId") Long publisherId, Pageable pageable);
    
    @Query("SELECT b FROM Book b WHERE b.language = :language")
    Page<Book> findByLanguage(@Param("language") String language, Pageable pageable);
    
    @Query("SELECT b FROM Book b WHERE b.publicationYear = :year")
    Page<Book> findByPublicationYear(@Param("year") Integer year, Pageable pageable);
    
    @Query("SELECT DISTINCT b.language FROM Book b")
    List<String> findAllLanguages();
} 