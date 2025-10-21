package hu.renew.main.controller;

import hu.renew.main.dto.ContactMessageRequest;
import hu.renew.main.dto.ContactMessageResponse;
import hu.renew.main.service.ContactMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactMessageController {

    private final ContactMessageService contactMessageService;

    @PostMapping
    public ResponseEntity<ContactMessageResponse> sendMessage(@Valid @RequestBody ContactMessageRequest request) {
        ContactMessageResponse response = contactMessageService.saveMessage(request);
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


