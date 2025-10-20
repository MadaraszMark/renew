package hu.renew.main.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import hu.renew.main.dto.AuthResponse;
import hu.renew.main.dto.RegisterRequest;
import hu.renew.main.model.User;

@Component
public class UserMapper {

    public User toEntity(RegisterRequest request, String encodedPassword) {
        return User.builder()
                .email(request.getEmail())
                .passwordHash(encodedPassword)
                .role(request.getRole() != null ? request.getRole() : "USER")
                .createdAt(LocalDateTime.now())
                .build();
    }

    public AuthResponse toResponse(User user, String message) {
        return AuthResponse.builder()
                .email(user.getEmail())
                .role(user.getRole())
                .message(message)
                .build();
    }
}

