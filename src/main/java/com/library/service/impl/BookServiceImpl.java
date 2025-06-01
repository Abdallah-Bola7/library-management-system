package com.library.service.impl;

import com.library.dto.BookDTO;
import com.library.mapper.BookMapper;
import com.library.model.Author;
import com.library.model.Book;
import com.library.model.Category;
import com.library.model.Publisher;
import com.library.repository.AuthorRepository;
import com.library.repository.BookRepository;
import com.library.repository.CategoryRepository;
import com.library.repository.PublisherRepository;
import com.library.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private static final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

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
        log.debug("Creating new book: {}", bookDTO);
        Book book = bookMapper.toEntity(bookDTO);
        updateBookRelations(book, bookDTO);
        book = bookRepository.save(book);
        return bookMapper.toDTO(book);
    }

    @Override
    public Optional<BookDTO> updateBook(Long id, BookDTO bookDTO) {
        log.debug("Updating book with ID: {}", id);
        return bookRepository.findById(id)
                .map(existingBook -> {
                    Book updatedBook = bookMapper.toEntity(bookDTO);
                    updatedBook.setId(id);
                    updateBookRelations(updatedBook, bookDTO);
                    return bookMapper.toDTO(bookRepository.save(updatedBook));
                });
    }

    @Override
    public Optional<BookDTO> getBook(Long id) {
        log.debug("Fetching book with ID: {}", id);
        return bookRepository.findById(id)
                .map(bookMapper::toDTO);
    }

    @Override
    public boolean deleteBook(Long id) {
        log.debug("Deleting book with ID: {}", id);
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Page<BookDTO> getAllBooks(Pageable pageable) {
        log.debug("Fetching all books with pagination");
        return bookRepository.findAll(pageable)
                .map(bookMapper::toDTO);
    }

    @Override
    public Page<BookDTO> searchBooks(String query, Pageable pageable) {
        log.debug("Searching books with query: {} and pagination", query);
        return bookRepository.findByTitleContainingIgnoreCase(query, pageable)
                .map(bookMapper::toDTO);
    }

    @Override
    public Page<BookDTO> getBooksByAuthor(Long authorId, Pageable pageable) {
        log.debug("Fetching books by author ID: {} with pagination", authorId);
        return bookRepository.findByAuthorId(authorId, pageable)
                .map(bookMapper::toDTO);
    }

    @Override
    public Page<BookDTO> getBooksByCategory(Long categoryId, Pageable pageable) {
        log.debug("Fetching books by category ID: {} with pagination", categoryId);
        return bookRepository.findByCategoryId(categoryId, pageable)
                .map(bookMapper::toDTO);
    }

    @Override
    public Page<BookDTO> getAvailableBooks(Pageable pageable) {
        log.debug("Fetching available books with pagination");
        return bookRepository.findAvailableBooks(pageable)
                .map(bookMapper::toDTO);
    }

    @Override
    public Page<BookDTO> getBooksByPublisher(Long publisherId, Pageable pageable) {
        log.debug("Fetching books by publisher ID: {} with pagination", publisherId);
        return bookRepository.findByPublisherId(publisherId, pageable)
                .map(bookMapper::toDTO);
    }

    @Override
    public Page<BookDTO> getBooksByLanguage(String language, Pageable pageable) {
        log.debug("Fetching books by language: {} with pagination", language);
        return bookRepository.findByLanguage(language, pageable)
                .map(bookMapper::toDTO);
    }

    @Override
    public Page<BookDTO> getBooksByYear(Integer year, Pageable pageable) {
        log.debug("Fetching books by year: {} with pagination", year);
        return bookRepository.findByPublicationYear(year, pageable)
                .map(bookMapper::toDTO);
    }

    @Override
    public Optional<Boolean> isBookAvailable(Long id) {
        log.debug("Checking availability for book ID: {}", id);
        return bookRepository.findById(id)
                .map(book -> book.getAvailableCopies() > 0);
    }

    @Override
    public void updateBookAvailability(Long bookId, int change) {
        log.debug("Updating availability for book ID: {} by {}", bookId, change);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        book.setAvailableCopies(book.getAvailableCopies() + change);
        bookRepository.save(book);
    }

    private void updateBookRelations(Book book, BookDTO dto) {
        if (dto.getAuthorIds() != null) {
            Set<Author> authors = dto.getAuthorIds().stream()
                    .map(id -> authorRepository.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException("Author not found: " + id)))
                    .collect(Collectors.toSet());
            book.setAuthors(authors);
        }

        if (dto.getCategoryIds() != null) {
            Set<Category> categories = dto.getCategoryIds().stream()
                    .map(id -> categoryRepository.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException("Category not found: " + id)))
                    .collect(Collectors.toSet());
            book.setCategories(categories);
        }

        if (dto.getPublisherId() != null) {
            Publisher publisher = publisherRepository.findById(dto.getPublisherId())
                    .orElseThrow(() -> new EntityNotFoundException("Publisher not found: " + dto.getPublisherId()));
            book.setPublisher(publisher);
        }
    }
} 