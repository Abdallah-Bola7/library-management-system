package com.library.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class CategoryDTO {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    private String description;
    private Long parentId;
    private Set<Long> childrenIds = new HashSet<>();
    private Set<Long> bookIds = new HashSet<>();
} 