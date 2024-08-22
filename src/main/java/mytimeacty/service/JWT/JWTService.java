package mytimeacty.service.JWT;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import mytimeacty.exception.UnauthoriedException;
import mytimeacty.model.users.dto.UserDTO;

@Service
public class JWTService {

	@Autowired
	private JwtEncoder jwtEncoder;
	
	@Autowired
    private JwtDecoder jwtDecoder;
	
	private static final Logger logger = LoggerFactory.getLogger(JWTService.class);
	
	/**
	 * Generates a JWT (JSON Web Token) for the authenticated user.
	 * 
	 * This method creates a JWT containing user-specific claims, such as email, nickname, and user ID.
	 * The token is signed using the HS256 algorithm and has a 2-hour expiration time.
	 * 
	 * @param authentication the authenticated user's data (UserDTO) containing email, nickname, and user ID.
	 * @return a signed JWT as a string, which can be used for authentication and authorization.
	 */
	public String generateToken(UserDTO authentication) {
    	Instant now = Instant.now();
 		JwtClaimsSet claims = JwtClaimsSet.builder()
           		 .issuedAt(now)
          		  .expiresAt(now.plus(2, ChronoUnit.HOURS))
          		  .subject(authentication.getEmail())
          		  .claim("nickname", authentication.getNickname())
          		  .claim("id", authentication.getIdUser())
          		  .build();
		JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
		return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
	}
	
	public String generateVerificationToken(UserDTO user) {
		Instant now = Instant.now();
 		JwtClaimsSet claims = JwtClaimsSet.builder()
           		 .issuedAt(now)
          		  .expiresAt(now.plus(1, ChronoUnit.HOURS))
          		  .subject(user.getEmail())
          		  .build();
		JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
		return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
	}
	
	public String validateTokenAndGetEmail(String token) {
        try {
            Jwt jwt = this.getJwtFromToken(token);

            String email = jwt.getSubject();

            return email;
        } catch (JwtException | IllegalArgumentException e) {
        	logger.warn("Invalid token or token expired");
            throw new UnauthoriedException("Invalid token or token expired");
        }
    }

	
	/**
	  * Decodes the JWT token to extract its claims.
	  * 
	  * @param token the JWT token to be decoded.
	  * @return a Jwt object representing the decoded token.
	  */
	 public Jwt getJwtFromToken(String token) {
		return jwtDecoder.decode(token);  
	 }
	
}
