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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public TransactionResponseDto createTransaction(Long userId, TransactionRequestDto request) {
        if (!userRepository.existsById(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

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

    // ★ 1. 수정 메서드
    @Transactional
    public TransactionResponseDto updateTransaction(Long userId, Long transactionId, TransactionRequestDto request) {
        // 본인 거래 내역 확인
        Transaction transaction = transactionRepository.findByIdAndUserId(transactionId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRANSACTION_NOT_FOUND));

        // 카테고리 확인
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        // 더티 체킹을 통한 수정
        transaction.update(
                category.getId(),
                category.getType(),
                request.getAmount(),
                request.getDescription(),
                request.getTransactionDate()
        );

        return new TransactionResponseDto(transaction, category.getName());
    }

    // ★ 2. 삭제 메서드 (새로 추가됨!)
    @Transactional
    public void deleteTransaction(Long userId, Long transactionId) {
        // 삭제할 내역이 본인 소유인지 검증 후 조회
        Transaction transaction = transactionRepository.findByIdAndUserId(transactionId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRANSACTION_NOT_FOUND));

        transactionRepository.delete(transaction);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactions(Long userId) {
        List<Transaction> transactions = transactionRepository.findByUserIdOrderByTransactionDateDesc(userId);

        Map<Long, String> categoryNameMap = categoryRepository.findByUserId(userId).stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));

        return transactions.stream()
                .map(t -> new TransactionResponseDto(
                        t,
                        categoryNameMap.getOrDefault(t.getCategoryId(), "미분류")
                ))
                .collect(Collectors.toList());
    }
}