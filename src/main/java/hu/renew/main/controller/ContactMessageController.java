package hu.renew.main.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.renew.main.dto.ContactMessageRequest;
import hu.renew.main.dto.ContactMessageResponse;
import hu.renew.main.model.User;
import hu.renew.main.repository.UserRepository;
import hu.renew.main.security.JwtTokenUtil;
import hu.renew.main.service.ContactMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", exposedHeaders = "Authorization")
public class ContactMessageController {

    private final ContactMessageService contactMessageService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtUtil;

    @PostMapping
    public ResponseEntity<ContactMessageResponse> sendMessage(@Valid @RequestBody ContactMessageRequest request,Authentication authentication) {

        String senderEmail;
        if (authentication != null && authentication.isAuthenticated()) {
            senderEmail = authentication.getName();

        } else {
            senderEmail = request.getEmail();

            User guest = userRepository.findByEmail(senderEmail).orElseGet(() -> {
                User newGuest = new User();
                newGuest.setEmail(senderEmail);
                newGuest.setPasswordHash(passwordEncoder.encode("guest123"));
                newGuest.setRole("GUEST");
                return userRepository.save(newGuest);
            });

            String token = jwtUtil.generateToken(guest.getEmail(), guest.getRole());

            ContactMessageResponse response = contactMessageService.saveMessageForUser(request, senderEmail);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .header("Authorization", "Bearer " + token)
                    .body(response);
        }

        ContactMessageResponse response = contactMessageService.saveMessageForUser(request, senderEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



    @GetMapping
    public ResponseEntity<Page<ContactMessageResponse>> getMessages(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size,Authentication auth) {

        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        String userEmail = auth.getName();

        Page<ContactMessageResponse> messages =contactMessageService.getMessages(page, size, isAdmin, userEmail);

        return ResponseEntity.ok(messages);
    }
}


