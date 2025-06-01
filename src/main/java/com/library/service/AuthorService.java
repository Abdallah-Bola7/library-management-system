package com.library.service;

import com.library.dto.AuthorDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface AuthorService {
    AuthorDTO createAuthor(AuthorDTO authorDTO);
    Optional<AuthorDTO> updateAuthor(Long id, AuthorDTO authorDTO);
    Optional<AuthorDTO> getAuthor(Long id);
    boolean deleteAuthor(Long id);
    Page<AuthorDTO> getAllAuthors(Pageable pageable);
    Page<AuthorDTO> searchAuthors(String query, Pageable pageable);
    Page<AuthorDTO> getAuthorsByNationality(String nationality, Pageable pageable);
} 