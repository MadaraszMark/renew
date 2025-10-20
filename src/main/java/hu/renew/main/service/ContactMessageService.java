package hu.renew.main.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        if (request.getMessage().length() < 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Az üzenet túl rövid!");
        }

        ContactMessage entity = contactMessageMapper.toEntity(request);
        ContactMessage saved = contactMessageRepository.save(entity);
        return contactMessageMapper.toResponse(saved);
    }

    public Page<ContactMessageResponse> getAllMessages(Pageable pageable) {
        return contactMessageRepository.findAll(pageable).map(contactMessageMapper::toResponse);
    }
}

