package mytimeacty.security;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import mytimeacty.annotation.RolesAllowed;
import mytimeacty.model.users.UserDTO;
import mytimeacty.utils.SecurityUtils;

@Aspect
@Component
public class RoleAspect {

    @Before("@annotation(rolesAllowed)")
    public void checkRolesAllowed(RolesAllowed rolesAllowed) {
        UserDTO user = SecurityUtils.getCurrentUser();

        boolean hasRole = false;

        for (String role : rolesAllowed.value()) {
            if (user.getUserRole().equals(role)) {
                hasRole = true;
                break;
            }
        }
        
        if (!hasRole) {
            throw new SecurityException("Forbidden: Insufficient roles");
        }
    }
}