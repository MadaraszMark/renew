package hu.renew.main.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import hu.renew.main.model.User;
import hu.renew.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CurrentUser {

    private final UserRepository userRepository;

    public String getEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Nincs bejelentkezve.");
        }
        return auth.getPrincipal().toString();
    }

    public User requireEntity() {
        String email = getEmail();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Felhasználó nem található."));
    }

    public Integer requireId() {
        return requireEntity().getId();
    }
}

