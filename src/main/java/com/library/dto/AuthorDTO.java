package com.library.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class AuthorDTO {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    private String biography;
    private String nationality;
    private String birthDate;
    private Set<Long> bookIds = new HashSet<>();
} 