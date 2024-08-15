package mytimeacty.model.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserCreateDTO {
	
	@NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
	
	@NotBlank(message = "Nickname is required")
    private String nickname;
	
	@NotBlank(message = "Password is required")
    private String password;
}