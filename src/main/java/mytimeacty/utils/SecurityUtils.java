package mytimeacty.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import mytimeacty.model.users.dto.UserDTO;

public class SecurityUtils {

    public static UserDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDTO) {
            return (UserDTO) authentication.getPrincipal();
        } else {
            throw new IllegalStateException("User not authenticated or invalid principal");
        }
    }
}
