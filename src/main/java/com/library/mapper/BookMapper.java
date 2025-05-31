package com.library.mapper;

import com.library.dto.BookDTO;
import com.library.model.Author;
import com.library.model.Book;
import com.library.model.Category;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {})
public interface BookMapper {

    @Mapping(target = "authorIds", source = "authors", qualifiedByName = "authorsToAuthorIds")
    @Mapping(target = "categoryIds", source = "categories", qualifiedByName = "categoriesToCategoryIds")
    @Mapping(target = "publisherId", source = "publisher.id")
    BookDTO toDTO(Book book);

    @InheritInverseConfiguration
    @Mapping(target = "authors", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "publisher", ignore = true)
    @Mapping(target = "borrowRecords", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Book toEntity(BookDTO bookDTO);

    @Named("authorsToAuthorIds")
    default Set<Long> authorsToAuthorIds(Set<Author> authors) {
        if (authors == null) {
            return null;
        }
        return authors.stream()
                .map(Author::getId)
                .collect(Collectors.toSet());
    }

    @Named("categoriesToCategoryIds")
    default Set<Long> categoriesToCategoryIds(Set<Category> categories) {
        if (categories == null) {
            return null;
        }
        return categories.stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
    }

    @AfterMapping
    default void updateBookDTOFromEntity(@MappingTarget BookDTO bookDTO, Book book) {
        if (book.getPublisher() != null) {
            bookDTO.setPublisherId(book.getPublisher().getId());
        }
    }
} 