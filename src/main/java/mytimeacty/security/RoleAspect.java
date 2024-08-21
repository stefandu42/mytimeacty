package mytimeacty.security;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import mytimeacty.annotation.RolesAllowed;
import mytimeacty.model.users.dto.UserDTO;
import mytimeacty.model.users.enums.UserRole;
import mytimeacty.utils.SecurityUtils;

@Aspect
@Component
public class RoleAspect {

	private static final Logger logger = LoggerFactory.getLogger(SecurityUtils.class);
	
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
    public void checkRolesAllowed(JoinPoint joinPoint, RolesAllowed rolesAllowed) {
        UserDTO user = SecurityUtils.getCurrentUser();

        boolean hasRole = false;
        String[] allowedRoles = rolesAllowed.value();

        for (String role : rolesAllowed.value()) {
            if (user.getUserRole().equals(UserRole.fromString(role).getRole())) {
                hasRole = true;
                break;
            }
        }
        
        if (!hasRole) {
        	// Getting the method signature for logging
        	String methodName = joinPoint.getSignature().getName();

            logger.warn("User with nickname '{}' has insufficient roles to perform this action. (Actual role: '{}', Required roles: '{}', Method: '{}')", 
                        user.getNickname(), 
                        user.getUserRole(), 
                        String.join(", ", allowedRoles),
                        methodName);
            throw new SecurityException("Forbidden: Insufficient roles");
        }
    }
}