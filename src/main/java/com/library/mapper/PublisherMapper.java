package com.library.mapper;

import com.library.dto.PublisherDTO;
import com.library.model.Book;
import com.library.model.Publisher;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {})
public interface PublisherMapper {

    @Mapping(target = "bookIds", source = "books", qualifiedByName = "booksToBookIds")
    PublisherDTO toDTO(Publisher publisher);

    @InheritInverseConfiguration
    @Mapping(target = "books", ignore = true)
    Publisher toEntity(PublisherDTO publisherDTO);

    @Named("booksToBookIds")
    default Set<Long> booksToBookIds(Set<Book> books) {
        if (books == null) {
            return null;
        }
        return books.stream()
                .map(Book::getId)
                .collect(Collectors.toSet());
    }

    @AfterMapping
    default void updatePublisherDTOFromEntity(@MappingTarget PublisherDTO publisherDTO, Publisher publisher) {
        if (publisher.getBooks() != null) {
            publisherDTO.setBookIds(booksToBookIds(publisher.getBooks()));
        }
    }
} 