package com.library.service.impl;

import com.library.dto.PublisherDTO;
import com.library.mapper.PublisherMapper;
import com.library.model.Publisher;
import com.library.repository.PublisherRepository;
import com.library.service.PublisherService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class PublisherServiceImpl implements PublisherService {

    private static final Logger log = LoggerFactory.getLogger(PublisherServiceImpl.class);

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private PublisherMapper publisherMapper;

    @Override
    public PublisherDTO createPublisher(PublisherDTO publisherDTO) {
        log.debug("Creating new publisher: {}", publisherDTO);
        Publisher publisher = publisherMapper.toEntity(publisherDTO);
        publisher = publisherRepository.save(publisher);
        return publisherMapper.toDTO(publisher);
    }

    @Override
    public Optional<PublisherDTO> updatePublisher(Long id, PublisherDTO publisherDTO) {
        log.debug("Updating publisher with ID: {}", id);
        return publisherRepository.findById(id)
                .map(existingPublisher -> {
                    Publisher updatedPublisher = publisherMapper.toEntity(publisherDTO);
                    updatedPublisher.setId(id);
                    return publisherMapper.toDTO(publisherRepository.save(updatedPublisher));
                });
    }

    @Override
    public Optional<PublisherDTO> getPublisher(Long id) {
        log.debug("Fetching publisher with ID: {}", id);
        return publisherRepository.findById(id)
                .map(publisherMapper::toDTO);
    }

    @Override
    public boolean deletePublisher(Long id) {
        log.debug("Deleting publisher with ID: {}", id);
        if (publisherRepository.existsById(id)) {
            publisherRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Page<PublisherDTO> getAllPublishers(Pageable pageable) {
        log.debug("Fetching all publishers with pagination");
        return publisherRepository.findAll(pageable)
                .map(publisherMapper::toDTO);
    }

    @Override
    public Page<PublisherDTO> searchPublishers(String query, Pageable pageable) {
        log.debug("Searching publishers with query: {} and pagination", query);
        return publisherRepository.searchPublishers(query, pageable)
                .map(publisherMapper::toDTO);
    }

    @Override
    public Page<PublisherDTO> getPublishersByCountry(String country, Pageable pageable) {
        log.debug("Fetching publishers by country: {} with pagination", country);
        return publisherRepository.findByCountry(country, pageable)
                .map(publisherMapper::toDTO);
    }

    @Override
    public Page<PublisherDTO> getMostActivePublishers(Pageable pageable) {
        log.debug("Fetching most active publishers with pagination");
        return publisherRepository.findMostActivePublishers(pageable)
                .map(publisherMapper::toDTO);
    }
} 