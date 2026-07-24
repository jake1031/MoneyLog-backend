package com.example.backend.service;

import com.example.backend.dto.TransactionRequestDto;
import com.example.backend.dto.TransactionResponseDto;
import com.example.backend.entity.Category;
import com.example.backend.entity.Transaction;
import com.example.backend.global.common.exception.CustomException;
import com.example.backend.global.common.exception.ErrorCode;
import com.example.backend.repository.CategoryRepository;
import com.example.backend.repository.TransactionRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TransactionService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public TransactionService(UserRepository userRepository,
                              TransactionRepository transactionRepository,
                              CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public TransactionResponseDto createTransaction(Long userId, TransactionRequestDto request) { // ★ userId 파라미터 추가
        // 1. 유저 존재 여부 확인 (필요 시 SecurityContext로 대체 가능하나 유효성 검증용)
        if (!userRepository.existsById(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED); //
        }

        // 2. 카테고리 존재 여부 확인
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND)); //[cite: 1, 2]

        Transaction transaction = new Transaction(
                userId,
                category.getId(),
                category.getType(),
                request.getAmount(),
                request.getDescription(),
                request.getTransactionDate()
        );

        Transaction savedTransaction = transactionRepository.save(transaction);

        return new TransactionResponseDto(savedTransaction, category.getName());
    }

    // ★ [참고] 수정/삭제/단건 조회 메서드 작성 시 패턴
    @Transactional
    public TransactionResponseDto updateTransaction(Long userId, Long transactionId, TransactionRequestDto request) {
        // 1. 본인의 거래 내역 조회 (없으면 예외)
        Transaction transaction = transactionRepository.findByIdAndUserId(transactionId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRANSACTION_NOT_FOUND));

        // 2. 카테고리 존재 여부 확인 및 엔티티 조회
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        // 3. 더티 체킹(Dirty Checking)을 활용한 데이터 수정
        // (엔티티의 필드값만 바꾸면 @Transactional에 의해 메서드 종료 시 자동 UPDATE 실행됨)
        transaction.update(
                category.getId(),
                category.getType(),
                request.getAmount(),
                request.getDescription(),
                request.getTransactionDate()
        );

        // save()를 명시적으로 호출하지 않아도 DB에 반영됩니다.
        return new TransactionResponseDto(transaction, category.getName());
    }
}