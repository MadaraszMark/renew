package hu.renew.main.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import hu.renew.main.dto.ContactMessageRequest;
import hu.renew.main.dto.ContactMessageResponse;
import hu.renew.main.mapper.ContactMessageMapper;
import hu.renew.main.model.ContactMessage;
import hu.renew.main.repository.ContactMessageRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;
    private final ContactMessageMapper contactMessageMapper;

    public ContactMessageResponse saveMessage(ContactMessageRequest request) {
        if (request.getMessage().length() < 10) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Az üzenet túl rövid! Minimum 10 karakter szükséges.");
        }

        ContactMessage entity = contactMessageMapper.toEntity(request);
        entity.setCreatedAt(java.time.LocalDateTime.now());
        ContactMessage saved = contactMessageRepository.save(entity);
        return contactMessageMapper.toResponse(saved);
    }

    public Page<ContactMessageResponse> getAllMessages(Pageable pageable) {
        return contactMessageRepository.findAll(pageable).map(contactMessageMapper::toResponse);
    }

    public Page<ContactMessageResponse> getMessages(int page, int size, boolean isAdmin, String userEmail) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<ContactMessage> messagePage = isAdmin? contactMessageRepository.findAll(pageable): contactMessageRepository.findByEmail(userEmail, pageable);
        return messagePage.map(contactMessageMapper::toResponse);
    }
}


