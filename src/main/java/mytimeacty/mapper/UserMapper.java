package mytimeacty.mapper;

import mytimeacty.model.users.User;
import mytimeacty.model.users.dto.UserDTO;

public class UserMapper {

	/**
	 * Converts a User entity to a UserDTO.
	 * 
	 * @param user the User entity to be converted.
	 * @return a UserDTO representing the user's information, or null if the input user is null.
	 */
    public static UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        return UserDTO.builder()
                .idUser(user.getIdUser())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .userRole(user.getUserRole())
                .previousRole(user.getUserPreviousRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
