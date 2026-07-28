package com.example.backend.global.config;

import com.example.backend.dto.CategoryRequestDto;
import com.example.backend.entity.Category;
import com.example.backend.entity.CategoryType;
import com.example.backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private static final Long SYSTEM_USER_ID = 0L;

    @Override
    public void run(String... args) {
        // SYSTEM_USER_ID(0L)로 등록된 카테고리가 없을 때만 초기 데이터 입력
        if (!categoryRepository.existsByUserId(SYSTEM_USER_ID)) {
            List<Category> defaultCategories = List.of(
                    // 지출(EXPENSE) 기본 카테고리
                    new Category(SYSTEM_USER_ID, "식비", CategoryType.EXPENSE),
                    new Category(SYSTEM_USER_ID, "교통", CategoryType.EXPENSE),
                    new Category(SYSTEM_USER_ID, "쇼핑", CategoryType.EXPENSE),
                    new Category(SYSTEM_USER_ID, "문화/취미", CategoryType.EXPENSE),
                    new Category(SYSTEM_USER_ID, "생활", CategoryType.EXPENSE),

                    // 수입(INCOME) 기본 카테고리
                    new Category(SYSTEM_USER_ID, "월급", CategoryType.INCOME),
                    new Category(SYSTEM_USER_ID, "용돈", CategoryType.INCOME),
                    new Category(SYSTEM_USER_ID, "부수입", CategoryType.INCOME)
            );

            categoryRepository.saveAll(defaultCategories);
        }
    }
}
