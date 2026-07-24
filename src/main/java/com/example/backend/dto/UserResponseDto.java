package com.example.backend.dto;

import com.example.backend.entity.User;

public class UserResponseDto {

    private Long id;
    private String email;
    private String Nickname;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.Nickname = user.getNickname();
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getName() { return Nickname; }
}