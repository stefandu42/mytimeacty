package mytimeacty.filter;

import jakarta.servlet.Filter; 
import jakarta.servlet.FilterChain; 
import jakarta.servlet.ServletException; 
import jakarta.servlet.ServletRequest; 
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mytimeacty.model.users.dto.UserDTO;
import mytimeacty.model.users.enums.UserRole;
import mytimeacty.service.UserService;
import mytimeacty.service.JWT.JWTService;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;


public class JwtAuthenticationFilter implements Filter { 
	
	@Autowired
	private UserService userService;
	
	@Autowired
    private JWTService jwtService;
	
	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
	
	/**
	 * Custom filter for handling authentication and authorization.
	 * This filter checks the validity of JWT tokens and manages user roles.
	 * 
	 * @param servletRequest the request to be processed.
	 * @param servletResponse the response to be sent.
	 * @param filterChain the filter chain to pass the request and response through.
	 * @throws IOException if an I/O error occurs during filtering.
	 * @throws ServletException if a servlet error occurs during filtering.
	 */
	@Override
	public void doFilter(ServletRequest servletRequest, 
						ServletResponse servletResponse, 
						FilterChain filterChain) 
		throws IOException, ServletException{ 

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = request.getRequestURI();

        if (path.startsWith("/auth")) {
        	filterChain.doFilter(servletRequest, servletResponse);
        	return;
        }
        
        String token = resolveToken(request);
        
        if(token == null){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing Token");
            logger.warn("Missing Token", token);
            return;
        }
        
        UserDTO userFromToken = getUserFromToken(token);
        
        if(userFromToken.getUserRole().equals(UserRole.BANNED.getRole())){
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are banned");
            logger.warn("User banned with nickname '{}' tried to make a request", userFromToken.getNickname());
            return;
        }
    	
        Authentication authentication = getAuthentication(userFromToken);
        if (authentication != null) 
        	SecurityContextHolder.getContext().setAuthentication(authentication);
        
    	logger.info("Entering method favouriteQuizz: User '{}'", userFromToken.getNickname());
       
        filterChain.doFilter(servletRequest, servletResponse);
        
	} 
	 
	/**
	 * Resolves the JWT token from the Authorization header.
	 * 
	 * @param request the HTTP request from which to extract the token.
	 * @return the JWT token, or null if not present or invalid.
	 */
	public String resolveToken(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
	    if (header != null && !header.isBlank() && header.startsWith("Bearer ")) {
	        return header.substring(7);  
	    }
	    return null;
	}
	
	/**
	 * Retrieves user details from the JWT token.
	 * 
	 * @param token the JWT token containing user information.
	 * @return a UserDTO object with user details.
	 */
	public UserDTO getUserFromToken(String token) {
		Jwt jwt = jwtService.getJwtFromToken(token);

		long userId = jwt.getClaim("id");

	    UserDTO user = userService.getUserById((int) userId);
	    return user;
	}
	
	/**
	 * Creates an Authentication object based on user details and roles.
	 * 
	 * @param user the UserDTO object containing user details.
	 * @return an Authentication object for the user.
	 */
	 public Authentication getAuthentication(UserDTO user) {
		 String role = user.getUserRole();

		 List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role.toUpperCase()));
		 return new UsernamePasswordAuthenticationToken(user, null, authorities);
	 }
	 
   
}
