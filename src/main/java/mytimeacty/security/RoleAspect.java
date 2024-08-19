package mytimeacty.security;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import mytimeacty.annotation.RolesAllowed;
import mytimeacty.model.users.dto.UserDTO;
import mytimeacty.model.users.enums.UserRole;
import mytimeacty.utils.SecurityUtils;

@Aspect
@Component
public class RoleAspect {

	/**
	 * Aspect method that checks if the current user has the required roles before executing a method.
	 * 
	 * This method is triggered by the @RolesAllowed annotation. It checks if the currently authenticated user
	 * has one of the roles specified in the annotation. If the user does not have the required role, a 
	 * SecurityException is thrown.
	 * 
	 * @param rolesAllowed the @RolesAllowed annotation containing the list of allowed roles.
	 * @throws SecurityException if the user does not have the necessary role to execute the method.
	 */
    @Before("@annotation(rolesAllowed)")
    public void checkRolesAllowed(RolesAllowed rolesAllowed) {
        UserDTO user = SecurityUtils.getCurrentUser();

        boolean hasRole = false;

        for (String role : rolesAllowed.value()) {
            if (user.getUserRole().equals(UserRole.fromString(role).getRole())) {
                hasRole = true;
                break;
            }
        }
        
        if (!hasRole) {
            throw new SecurityException("Forbidden: Insufficient roles");
        }
    }
}