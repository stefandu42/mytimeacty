package mytimeacty.service.JWT;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import mytimeacty.model.users.UserDTO;

@Service
public class JWTService {

	@Autowired
	private JwtEncoder jwtEncoder;
	
	
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
}
