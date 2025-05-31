package com.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class BookDTO {
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "ISBN is required")
    private String isbn;

    private Set<Long> authorIds = new HashSet<>();
    
    @NotNull(message = "Publisher ID is required")
    private Long publisherId;
    
    private Set<Long> categoryIds = new HashSet<>();
    
    private String language;
    private Integer publicationYear;
    private String edition;
    private String summary;
    private String coverImageUrl;
    
    @Positive(message = "Total copies must be positive")
    private Integer totalCopies;

    private Integer availableCopies;
} 