package com.library.service;

import com.library.dto.PublisherDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface PublisherService {
    PublisherDTO createPublisher(PublisherDTO publisherDTO);
    Optional<PublisherDTO> updatePublisher(Long id, PublisherDTO publisherDTO);
    Optional<PublisherDTO> getPublisher(Long id);
    boolean deletePublisher(Long id);
    Page<PublisherDTO> getAllPublishers(Pageable pageable);
    Page<PublisherDTO> searchPublishers(String query, Pageable pageable);
    Page<PublisherDTO> getPublishersByCountry(String country, Pageable pageable);
    Page<PublisherDTO> getMostActivePublishers(Pageable pageable);
} 