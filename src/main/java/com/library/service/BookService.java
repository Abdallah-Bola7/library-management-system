package com.library.service;

import com.library.dto.BookDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface BookService {
    BookDTO createBook(BookDTO bookDTO);
    Optional<BookDTO> updateBook(Long id, BookDTO bookDTO);
    Optional<BookDTO> getBook(Long id);
    boolean deleteBook(Long id);
    Page<BookDTO> getAllBooks(Pageable pageable);
    Page<BookDTO> searchBooks(String query, Pageable pageable);
    Page<BookDTO> getBooksByAuthor(Long authorId, Pageable pageable);
    Page<BookDTO> getBooksByCategory(Long categoryId, Pageable pageable);
    Page<BookDTO> getAvailableBooks(Pageable pageable);
    Page<BookDTO> getBooksByPublisher(Long publisherId, Pageable pageable);
    Page<BookDTO> getBooksByLanguage(String language, Pageable pageable);
    Page<BookDTO> getBooksByYear(Integer year, Pageable pageable);
    Optional<Boolean> isBookAvailable(Long id);
    void updateBookAvailability(Long bookId, int change);
} 