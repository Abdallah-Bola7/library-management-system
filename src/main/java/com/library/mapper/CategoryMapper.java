package com.library.mapper;

import com.library.dto.CategoryDTO;
import com.library.model.Book;
import com.library.model.Category;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {})
public interface CategoryMapper {

    @Mapping(target = "bookIds", source = "books", qualifiedByName = "booksToBookIds")
    @Mapping(target = "childrenIds", source = "children", qualifiedByName = "categoriesToIds")
    @Mapping(target = "parentId", source = "parent.id")
    CategoryDTO toDTO(Category category);

    @InheritInverseConfiguration
    @Mapping(target = "books", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Category toEntity(CategoryDTO categoryDTO);

    @Named("booksToBookIds")
    default Set<Long> booksToBookIds(Set<Book> books) {
        if (books == null) {
            return null;
        }
        return books.stream()
                .map(Book::getId)
                .collect(Collectors.toSet());
    }

    @Named("categoriesToIds")
    default Set<Long> categoriesToIds(Set<Category> categories) {
        if (categories == null) {
            return null;
        }
        return categories.stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
    }
} 