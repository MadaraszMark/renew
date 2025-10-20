package hu.renew.main.dto;

import jakarta.validation.constraints.NotBlank;
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
public class LoginRequest {
	
	@NotBlank(message = "E-mail megadása kötelező")
	private String email;
	
	@NotBlank(message = "A jelszó megadása kötelező")
	private String password;

}
