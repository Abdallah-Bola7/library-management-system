package com.library.service.impl;

import com.library.dto.AuthorDTO;
import com.library.mapper.AuthorMapper;
import com.library.model.Author;
import com.library.model.Book;
import com.library.repository.AuthorRepository;
import com.library.repository.BookRepository;
import com.library.service.AuthorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorMapper authorMapper;

    @Override
    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        Author author = authorMapper.toEntity(authorDTO);
        updateAuthorRelations(author, authorDTO);
        Author savedAuthor = authorRepository.save(author);
        return authorMapper.toDTO(savedAuthor);
    }

    @Override
    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + id));
        authorMapper.toEntity(authorDTO);
        updateAuthorRelations(author, authorDTO);
        Author updatedAuthor = authorRepository.save(author);
        return authorMapper.toDTO(updatedAuthor);
    }

    @Override
    public AuthorDTO getAuthor(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + id));
        return authorMapper.toDTO(author);
    }

    @Override
    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new EntityNotFoundException("Author not found with id: " + id);
        }
        authorRepository.deleteById(id);
    }

    @Override
    public Page<AuthorDTO> getAllAuthors(Pageable pageable) {
        return authorRepository.findAll(pageable).map(authorMapper::toDTO);
    }

    @Override
    public Page<AuthorDTO> searchAuthors(String query, Pageable pageable) {
        return authorRepository.findByNameContainingIgnoreCase(query, pageable)
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