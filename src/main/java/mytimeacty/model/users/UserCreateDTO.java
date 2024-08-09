package mytimeacty.model.users;

import lombok.Data;

@Data
public class UserCreateDTO {
    private String email;
    private String nickname;
    private String password;
}