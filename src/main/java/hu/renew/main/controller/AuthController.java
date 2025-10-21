package hu.renew.main.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import hu.renew.main.dto.AuthResponse;
import hu.renew.main.dto.LoginRequest;
import hu.renew.main.dto.RegisterRequest;
import hu.renew.main.model.User;
import hu.renew.main.security.JwtTokenUtil;
import hu.renew.main.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService service;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = service.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = service.findByEmail(request.getEmail());
        AuthResponse response = service.login(request);

        String token = jwtTokenUtil.generateToken(user.getEmail(), user.getRole());
        response.setToken(token);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout() {
        AuthResponse response = AuthResponse.builder().message("Sikeresen kijelentkeztél!").build();
        return ResponseEntity.ok(response);
    }
}
