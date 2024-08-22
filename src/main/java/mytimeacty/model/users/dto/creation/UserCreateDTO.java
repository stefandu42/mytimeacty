package mytimeacty.model.users.dto.creation;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCreateDTO {
	
	@NotBlank(message = "Email is required")
	@NotNull(message = "Email is required")
    @Email(message = "Invalid email format")
	@Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;
	
	@NotBlank(message = "Nickname is required")
	@NotNull(message = "Nickname is required")
	@Size(max = 20, message = "Nickname cannot exceed 20 characters")
    private String nickname;
	
	@NotBlank(message = "Password is required")
	@NotNull(message = "Password is required")
    private String password;
}