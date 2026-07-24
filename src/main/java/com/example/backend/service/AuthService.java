package com.example.backend.service;

import com.example.backend.dto.LoginRequestDto;
import com.example.backend.dto.SignUpRequestDto;
import com.example.backend.dto.TokenResponseDto;
import com.example.backend.dto.UserResponseDto;
import com.example.backend.entity.User;
import com.example.backend.global.security.JwtProvider;
import com.example.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Transactional
    public UserResponseDto signUp(SignUpRequestDto requestDto) {
        // 1. 이메일 중복 검증
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        // 3. User 엔티티 생성 및 저장
        User user = new User(requestDto.getEmail(), encodedPassword, requestDto.getName());
        User savedUser = userRepository.save(user);

        return new UserResponseDto(savedUser);
    }

    public TokenResponseDto login(LoginRequestDto requestDto) {
        // 1. 이메일로 유저 조회
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다."));

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        // 3. JWT 토큰 생성 및 반환
        String accessToken = jwtProvider.createAccessToken(user.getId());
        return new TokenResponseDto(accessToken);
    }
}