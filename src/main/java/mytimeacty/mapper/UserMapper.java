package mytimeacty.mapper;

import mytimeacty.model.users.User;
import mytimeacty.model.users.UserDTO;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        return UserDTO.builder()
                .idUser(user.getIdUser())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .userRole(user.getUserRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
