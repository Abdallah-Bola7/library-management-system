package com.library.mapper;

import com.library.dto.AuthorDTO;
import com.library.model.Author;
import com.library.model.Book;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {})
public interface AuthorMapper {

    @Mapping(target = "bookIds", source = "books", qualifiedByName = "booksToBookIds")
    AuthorDTO toDTO(Author author);

    @InheritInverseConfiguration
    @Mapping(target = "books", ignore = true)
    Author toEntity(AuthorDTO authorDTO);

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
    default void updateAuthorDTOFromEntity(@MappingTarget AuthorDTO authorDTO, Author author) {
        if (author.getBooks() != null) {
            authorDTO.setBookIds(booksToBookIds(author.getBooks()));
        }
    }
} 