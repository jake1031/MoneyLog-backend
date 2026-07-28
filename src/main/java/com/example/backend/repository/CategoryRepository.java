package com.example.backend.repository;

import com.example.backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // 내 데이터만 조회
    List<Category> findByUserId(Long userId);

    // user_id가 내 ID이거나, SYSTEM_ID(0L)인 데이터를 모두 조회
    List<Category> findByUserIdOrUserId(Long userId, Long systemUserId);

    // 수정/삭제 시 본인 카테고리가 맞는지 검증하며 조회
    Optional<Category> findByIdAndUserId(Long id, Long userId);

    boolean existsByUserId(Long systemUserId);
}