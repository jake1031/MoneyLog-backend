package com.example.backend.controller;

import com.example.backend.dto.TransactionRequestDto;
import com.example.backend.dto.TransactionResponseDto;
import com.example.backend.global.common.response.ApiResponse;
import com.example.backend.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TransactionResponseDto>> createTransaction(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody TransactionRequestDto requestDto) {

        TransactionResponseDto response = transactionService.createTransaction(userId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("거래내역이 등록되었습니다.", response));
    }

    // 👇 1. 거래 내역 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<TransactionResponseDto>>> getTransactions(
            @AuthenticationPrincipal Long userId) {

        List<TransactionResponseDto> response = transactionService.getTransactions(userId);

        return ResponseEntity.ok(ApiResponse.success("거래내역 목록 조회 성공", response));
    }

    // 1. 내역 수정 (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTransaction(
            @AuthenticationPrincipal Long userId, // SecurityContext에 저장된 userId
            @PathVariable Long id,
            @RequestBody TransactionRequestDto request) {

        // ★ 실제 DB 수정을 위해 서비스 메서드 호출!
        transactionService.updateTransaction(userId, id, request);

        return ResponseEntity.ok(ApiResponse.success("내역이 수정되었습니다."));
    }

    // 2. 내역 삭제 (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long id) {

        // ★ 실제 DB 삭제를 위해 서비스 메서드 호출!
        transactionService.deleteTransaction(userId, id);

        return ResponseEntity.ok(ApiResponse.success("삭제되었습니다."));
    }
}