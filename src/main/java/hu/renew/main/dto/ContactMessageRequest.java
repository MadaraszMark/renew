package hu.renew.main.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactMessageRequest {

    @NotBlank(message = "A név megadása kötelező.")
    @Size(max = 100, message = "A név legfeljebb 100 karakter lehet.")
    private String name;

    @NotBlank(message = "Az e-mail cím megadása kötelező.")
    @Email(message = "Érvényes e-mail címet adj meg.")
    private String email;

    @NotBlank(message = "Az üzenet megadása kötelező.")
    @Size(min = 5, max = 2000, message = "Az üzenet legalább 10 és legfeljebb 2000 karakter hosszú lehet.")
    private String message;
}

