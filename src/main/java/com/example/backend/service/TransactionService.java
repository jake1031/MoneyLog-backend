package com.example.backend.service;

import com.example.backend.dto.TransactionRequestDto;
import com.example.backend.dto.TransactionResponseDto;
import com.example.backend.entity.Category;
import com.example.backend.entity.Transaction;

import com.example.backend.entity.User;
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
    public TransactionResponseDto createTransaction(TransactionRequestDto request) {
        // 2일차에는 로그인한 유저 대신, 미리 시딩해둔 1번 유저를 가져와서 연결!
        User tempUser = userRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("테스트 유저가 없습니다."));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("카테고리가 없습니다."));

        Transaction transaction = new Transaction(1L, category.getId(),
                category.getType(), request.getAmount(),
                request.getDescription(), request.getTransactionDate());

        transaction =  transactionRepository.save(transaction);

        return new TransactionResponseDto(transaction, category.getName());
    }
}