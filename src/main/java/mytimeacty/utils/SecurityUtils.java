package mytimeacty.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import mytimeacty.model.users.dto.UserDTO;

public class SecurityUtils {

	/**
	 * Utility method to retrieve the currently authenticated user from the security context.
	 *
	 * @return the UserDTO of the currently authenticated user
	 * @throws IllegalStateException if the user is not authenticated or the principal is not of type UserDTO
	 */
    public static UserDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDTO) {
            return (UserDTO) authentication.getPrincipal();
        } else {
            throw new IllegalStateException("User not authenticated or invalid principal");
        }
    }
}
