package hu.renew.main.service;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import hu.renew.main.dto.AuthResponse;
import hu.renew.main.dto.LoginRequest;
import hu.renew.main.dto.RegisterRequest;
import hu.renew.main.mapper.UserMapper;
import hu.renew.main.model.User;
import hu.renew.main.repository.UserRepository;
import hu.renew.main.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ez az e-mail cím már használatban van!");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = userMapper.toEntity(request, encodedPassword);
        user.setCreatedAt(LocalDateTime.now());

        User saved = userRepository.save(user);

        String token = jwtTokenUtil.generateToken(user.getEmail(), user.getRole());

        return AuthResponse.builder()
                .email(saved.getEmail())
                .role(saved.getRole().toString())
                .token(token)
                .message("Sikeres regisztráció!")
                .build();
    }
    
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Felhasználó nem található"));
    }

    // 🔹 Bejelentkezés
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Hibás e-mail cím vagy jelszó."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Hibás e-mail cím vagy jelszó.");
        }

        String token = jwtTokenUtil.generateToken(user.getEmail(), user.getRole());

        return AuthResponse.builder()
                .email(user.getEmail())
                .role(user.getRole().toString())
                .token(token)
                .message("Sikeres bejelentkezés!")
                .build();
    }
}


