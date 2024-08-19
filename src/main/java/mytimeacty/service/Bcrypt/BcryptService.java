package mytimeacty.service.Bcrypt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BcryptService {

	@Autowired
    private BCryptPasswordEncoder passwordEncoder;
	
	/**
	 * Encodes the raw password using the BCrypt hashing algorithm.
	 * 
	 * @param rawPassword the plain text password to be encoded.
	 * @return the encoded password.
	 */
	public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
	
	/**
	 * Checks if the raw password matches the encoded password.
	 * 
	 * @param rawPassword the plain text password provided by the user.
	 * @param encodedPassword the encoded password stored in the database.
	 * @return true if the passwords match, false otherwise.
	 */
	public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
