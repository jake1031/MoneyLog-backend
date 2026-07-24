package com.example.backend.controller;

import com.example.backend.dto.LoginRequestDto;
import com.example.backend.dto.SignUpRequestDto;
import com.example.backend.dto.TokenResponseDto;
import com.example.backend.dto.UserResponseDto;
import com.example.backend.global.common.response.ApiResponse;
import com.example.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserResponseDto>> signUp(
            @Valid @RequestBody SignUpRequestDto requestDto) {

        UserResponseDto response = authService.signUp(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("회원가입이 완료되었습니다.", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponseDto>> login(
            @Valid @RequestBody LoginRequestDto requestDto) {

        TokenResponseDto tokenResponse = authService.login(requestDto);
        return ResponseEntity.ok(ApiResponse.success("로그인에 성공했습니다.", tokenResponse));
    }
}