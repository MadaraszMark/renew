package hu.renew.main.dto;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactMessageResponse {

    private Integer id;
    private String name;
    private String email;
    private String message;
    private LocalDateTime createdAt;
}

