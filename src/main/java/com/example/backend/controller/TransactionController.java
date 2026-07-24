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
}