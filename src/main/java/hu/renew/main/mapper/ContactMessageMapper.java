package hu.renew.main.mapper;

import org.springframework.stereotype.Component;

import hu.renew.main.dto.ContactMessageRequest;
import hu.renew.main.dto.ContactMessageResponse;
import hu.renew.main.model.ContactMessage;

@Component
public class ContactMessageMapper {

    public ContactMessage toEntity(ContactMessageRequest request) {
        return ContactMessage.builder()
                .name(request.getName())
                .email(request.getEmail())
                .message(request.getMessage())
                .build();
    }

    public ContactMessageResponse toResponse(ContactMessage entity) {
        return ContactMessageResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .message(entity.getMessage())
                .sentAt(entity.getCreatedAt())
                .build();
    }
}

