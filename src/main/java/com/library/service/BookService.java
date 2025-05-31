package com.library.service;

import com.library.dto.BookDTO;
import java.util.List;
import java.util.Optional;

public interface BookService {
    BookDTO createBook(BookDTO bookDTO);
    Optional<BookDTO> updateBook(Long id, BookDTO bookDTO);
    Optional<BookDTO> getBook(Long id);
    boolean deleteBook(Long id);
    List<BookDTO> getAllBooks();
    List<BookDTO> searchBooks(String query);
    List<BookDTO> getBooksByAuthor(Long authorId);
    List<BookDTO> getBooksByCategory(Long categoryId);
    List<BookDTO> getAvailableBooks();
    Optional<Boolean> isBookAvailable(Long id);
    void updateBookAvailability(Long bookId, int change);
} 