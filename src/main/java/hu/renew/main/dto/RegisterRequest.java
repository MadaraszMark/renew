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
public class RegisterRequest {
	
    @NotBlank(message = "Az e-mail nem lehet üres")
    @Email(message = "Érvényes e-mail címet adj meg")
	private String email;
	
	@NotBlank(message = "A jelszó megadása kötelező")
    @Size(min = 6, message = "A jelszónak legalább 6 karakter hosszúnak kell lennie")
	private String password;
	
	private String role = "USER";

}
