package com.library.service.impl;

import com.library.dto.AuthorDTO;
import com.library.mapper.AuthorMapper;
import com.library.model.Author;
import com.library.model.Book;
import com.library.repository.AuthorRepository;
import com.library.repository.BookRepository;
import com.library.service.AuthorService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    private static final Logger log = LoggerFactory.getLogger(AuthorServiceImpl.class);

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorMapper authorMapper;

    @Override
    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        log.debug("Creating new author: {}", authorDTO);
        Author author = authorMapper.toEntity(authorDTO);
        updateAuthorRelations(author, authorDTO);
        Author savedAuthor = authorRepository.save(author);
        return authorMapper.toDTO(savedAuthor);
    }

    @Override
    public Optional<AuthorDTO> updateAuthor(Long id, AuthorDTO authorDTO) {
        log.debug("Updating author with ID: {}", id);
        return authorRepository.findById(id)
                .map(existingAuthor -> {
                    Author updatedAuthor = authorMapper.toEntity(authorDTO);
                    updatedAuthor.setId(id);
                    updateAuthorRelations(updatedAuthor, authorDTO);
                    return authorMapper.toDTO(authorRepository.save(updatedAuthor));
                });
    }

    @Override
    public Optional<AuthorDTO> getAuthor(Long id) {
        log.debug("Fetching author with ID: {}", id);
        return authorRepository.findById(id)
                .map(authorMapper::toDTO);
    }

    @Override
    public boolean deleteAuthor(Long id) {
        log.debug("Deleting author with ID: {}", id);
        if (authorRepository.existsById(id)) {
            authorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Page<AuthorDTO> getAllAuthors(Pageable pageable) {
        log.debug("Fetching all authors with pagination");
        return authorRepository.findAll(pageable)
                .map(authorMapper::toDTO);
    }

    @Override
    public Page<AuthorDTO> searchAuthors(String query, Pageable pageable) {
        log.debug("Searching authors with query: {} and pagination", query);
        return authorRepository.searchAuthors(query, pageable)
                .map(authorMapper::toDTO);
    }

    @Override
    public Page<AuthorDTO> getAuthorsByNationality(String nationality, Pageable pageable) {
        log.debug("Fetching authors by nationality: {} with pagination", nationality);
        return authorRepository.findByNationality(nationality, pageable)
                .map(authorMapper::toDTO);
    }

    private void updateAuthorRelations(Author author, AuthorDTO dto) {
        if (dto.getBookIds() != null && !dto.getBookIds().isEmpty()) {
            Set<Book> books = new HashSet<>();
            for (Long bookId : dto.getBookIds()) {
                Book book = bookRepository.findById(bookId)
                        .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + bookId));
                books.add(book);
            }
            author.setBooks(books);
        }
    }
} 