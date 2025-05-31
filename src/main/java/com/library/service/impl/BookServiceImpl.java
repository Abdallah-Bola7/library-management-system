package com.library.service.impl;

import com.library.dto.BookDTO;
import com.library.mapper.BookMapper;
import com.library.model.Author;
import com.library.model.Book;
import com.library.model.Category;
import com.library.model.Publisher;
import com.library.repository.*;
import com.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private BookMapper bookMapper;

    @Override
    public BookDTO createBook(BookDTO bookDTO) {
        Book book = bookMapper.toEntity(bookDTO);
        updateBookRelations(book, bookDTO);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDTO(savedBook);
    }

    @Override
    public Optional<BookDTO> updateBook(Long id, BookDTO bookDTO) {
        return bookRepository.findById(id)
                .map(book -> {
                    bookMapper.toEntity(bookDTO);
                    updateBookRelations(book, bookDTO);
                    Book updatedBook = bookRepository.save(book);
                    return bookMapper.toDTO(updatedBook);
                });
    }

    @Override
    public Optional<BookDTO> getBook(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDTO);
    }

    @Override
    public boolean deleteBook(Long id) {
        return bookRepository.findById(id)
                .map(book -> {
                    bookRepository.delete(book);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDTO> searchBooks(String query) {
        return bookRepository.findByTitleContainingIgnoreCase(query).stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDTO> getBooksByAuthor(Long authorId) {
        return bookRepository.findByAuthorId(authorId).stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDTO> getBooksByCategory(Long categoryId) {
        return bookRepository.findByCategoryId(categoryId).stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDTO> getAvailableBooks() {
        return bookRepository.findAvailableBooks().stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Boolean> isBookAvailable(Long id) {
        return bookRepository.findById(id)
                .map(book -> book.getAvailableCopies() > 0);
    }

    @Override
    @Transactional
    public void updateBookAvailability(Long bookId, int change) {
        bookRepository.findById(bookId)
                .ifPresent(book -> {
                    int newAvailableCopies = book.getAvailableCopies() + change;
                    if (newAvailableCopies < 0 || newAvailableCopies > book.getTotalCopies()) {
                        throw new IllegalStateException("Invalid book availability update");
                    }
                    book.setAvailableCopies(newAvailableCopies);
                    bookRepository.save(book);
                });
    }

    private void updateBookRelations(Book book, BookDTO dto) {
        // Set publisher
        if (dto.getPublisherId() != null) {
            publisherRepository.findById(dto.getPublisherId())
                    .ifPresent(book::setPublisher);
        }

        // Set authors
        if (dto.getAuthorIds() != null && !dto.getAuthorIds().isEmpty()) {
            Set<Author> authors = new HashSet<>();
            for (Long authorId : dto.getAuthorIds()) {
                authorRepository.findById(authorId)
                        .ifPresent(authors::add);
            }
            book.setAuthors(authors);
        }

        // Set categories
        if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
            Set<Category> categories = new HashSet<>();
            for (Long categoryId : dto.getCategoryIds()) {
                categoryRepository.findById(categoryId)
                        .ifPresent(categories::add);
            }
            book.setCategories(categories);
        }
    }
} 