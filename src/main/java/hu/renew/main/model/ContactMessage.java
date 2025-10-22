package hu.renew.main.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contact_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "A név megadása kötelező!")
    @Size(max = 100, message = "A név legfeljebb 100 karakter lehet.")
    @Column(nullable = false, length = 100)
    private String name;

    @Email(message = "Érvényes email címet adj meg!")
    @NotBlank(message = "Az email megadása kötelező!")
    @Column(nullable = false, length = 150)
    private String email;

    @NotBlank(message = "Az üzenet nem lehet üres!")
    @Size(max = 1000, message = "Az üzenet legfeljebb 1000 karakter lehet.")
    @Column(nullable = false, length = 1000)
    private String message;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}

