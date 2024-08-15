package mytimeacty.model.auth;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {

	@NotBlank(message = "Nickaname/email is required")
	private String nicknameOrEmail;
	
	@NotBlank(message = "Password is required")
	private String password;
}
