package com.example.backend.service;

import com.example.backend.dto.CategoryRequestDto;
import com.example.backend.dto.CategoryResponseDto;
import com.example.backend.entity.Category;
import com.example.backend.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true) // 기본적으로 읽기 전용으로 설정하여 조회 성능 최적화
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * 1. 카테고리 생성
     */
    @Transactional
    public CategoryResponseDto createCategory(Long userId, CategoryRequestDto requestDto) {
        Category category = new Category(
                userId,
                requestDto.getName(),
                requestDto.getType()
        );

        category = categoryRepository.save(category);

        return new CategoryResponseDto(category);
    }

    /**
     * 2. 특정 유저의 카테고리 전체 목록 조회
     */
    public List<CategoryResponseDto> getCategories(Long userId) {
        return categoryRepository.findByUserId(userId)
                .stream()
                .map(CategoryResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 3. 카테고리 수정 (Dirty Checking 활용)
     */
    @Transactional
    public CategoryResponseDto updateCategory(Long userId, Long categoryId, CategoryRequestDto requestDto) {
        // 본인의 카테고리가 맞는지 검증하며 조회
        Category category = categoryRepository.findByIdAndUserId(categoryId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리를 찾을 수 없거나 접근 권한이 없습니다."));

        // 엔티티 필드 수정 -> 트랜잭션 종료 시점에 변경 감지로 UPDATE 쿼리 자동 실행
        category.update(requestDto.getName(), requestDto.getType());

        return new CategoryResponseDto(category);
    }

    /**
     * 4. 카테고리 삭제
     */
    @Transactional
    public void deleteCategory(Long userId, Long categoryId) {
        Category category = categoryRepository.findByIdAndUserId(categoryId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리를 찾을 수 없거나 접근 권한이 없습니다."));

        categoryRepository.delete(category);
    }
}