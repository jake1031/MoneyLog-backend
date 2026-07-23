package com.example.backend.controller;

import com.example.backend.dto.CategoryRequestDto;
import com.example.backend.dto.CategoryResponseDto;
import com.example.backend.global.common.response.ApiResponse;
import com.example.backend.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final Long userId = 1L;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // [POST] 카테고리 생성
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponseDto>> createCategory(
            @Valid @RequestBody CategoryRequestDto requestDto) {

        CategoryResponseDto response = categoryService.createCategory(userId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("카테고리 생성에 성공했습니다.", response));
    }

    // [GET] 카테고리 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponseDto>>> getCategories() {

        List<CategoryResponseDto> categories = categoryService.getCategories(userId);
        return ResponseEntity.ok(ApiResponse.success("카테고리 목록 조회에 성공했습니다.", categories));
    }

    // [PUT] 카테고리 수정
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> updateCategory(
            @PathVariable("id") Long categoryId,
            @Valid @RequestBody CategoryRequestDto requestDto) {

        CategoryResponseDto response = categoryService.updateCategory(userId, categoryId, requestDto);
        return ResponseEntity.ok(ApiResponse.success("카테고리 수정에 성공했습니다.", response));
    }

    // [DELETE] 카테고리 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(
            @PathVariable("id") Long categoryId) {

        categoryService.deleteCategory(userId, categoryId);
        return ResponseEntity.ok(ApiResponse.success("카테고리 삭제에 성공했습니다."));
    }
}
