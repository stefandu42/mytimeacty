package mytimeacty.service.Bcrypt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BcryptService {

	@Autowired
    private BCryptPasswordEncoder passwordEncoder;
	
	public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
	
	public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
