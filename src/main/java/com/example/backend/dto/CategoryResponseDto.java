package com.example.backend.dto;

import com.example.backend.entity.Category;
import com.example.backend.entity.CategoryType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CategoryResponseDto {
    private Long id;
    private Long userId;
    private String name;
    private CategoryType type;
    private LocalDateTime createdAt;

    public CategoryResponseDto(Category category) {
        this.id = category.getId();
        this.userId = category.getUserId();
        this.name = category.getName();
        this.type = category.getType();
        this.createdAt = category.getCreatedAt();
    }
}
