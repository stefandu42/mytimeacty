package mytimeacty.model.auth;


import lombok.Data;

@Data
public class LoginDTO {

	private String nicknameOrEmail;
	private String password;
}
