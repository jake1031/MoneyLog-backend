package com.example.backend.repository;

import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 1. 이메일로 유저 조회 (로그인 시 사용)
    Optional<User> findByEmail(String email);

    // 2. 이메일 중복 여부 확인 (회원가입 시 사용)
    boolean existsByEmail(String email);
}