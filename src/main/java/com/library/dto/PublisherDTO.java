package com.library.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class PublisherDTO {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;
    
    private String description;
    private String address;
    private String phone;
    private String email;
    private String website;
    private String country;
    private Set<Long> bookIds = new HashSet<>();
} 