package mytimeacty.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import mytimeacty.model.users.dto.UserDTO;

public class SecurityUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(SecurityUtils.class);

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
        	logger.error("User not authenticated or invalid principal");
            throw new IllegalStateException("User not authenticated or invalid principal");
        }
    }
}
