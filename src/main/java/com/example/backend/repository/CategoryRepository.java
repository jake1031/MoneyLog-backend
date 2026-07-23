package com.example.backend.repository;

import com.example.backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // 특정 유저의 모든 카테고리 목록 조회
    List<Category> findByUserId(Long userId);

    // 수정/삭제 시 본인 카테고리가 맞는지 검증하며 조회
    Optional<Category> findByIdAndUserId(Long id, Long userId);
}