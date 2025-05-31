package com.library.service;

import com.library.dto.AuthorDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorService {
    AuthorDTO createAuthor(AuthorDTO authorDTO);
    AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO);
    AuthorDTO getAuthor(Long id);
    void deleteAuthor(Long id);
    Page<AuthorDTO> getAllAuthors(Pageable pageable);
    Page<AuthorDTO> searchAuthors(String query, Pageable pageable);
} 