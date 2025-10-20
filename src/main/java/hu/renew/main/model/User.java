package hu.renew.main.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users", indexes = {@Index(name = "idx_users_email", columnList = "email", unique = true)})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Email(message = "Érvényes email címet adj meg!")
    @NotBlank(message = "Az email mező nem lehet üres!")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "A jelszó megadása kötelező!")
    @Size(min = 6, message = "A jelszónak legalább 6 karakter hosszúnak kell lennie!")
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(nullable = false, length = 20)
    private String role = "USER";

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}

