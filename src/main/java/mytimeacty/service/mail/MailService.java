package mytimeacty.service.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.profiles.active}")
    private String activeProfile;
    
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);
    
    public void sendVerificationEmail(String toEmail, String token) {
    	if ("prod".equals(activeProfile)) {
	        try {
	            String verificationLink = "https://www.mytimeacty.com/verify?token=" + token;
	            
	            SimpleMailMessage message = new SimpleMailMessage();
	            message.setTo(toEmail);
	            message.setSubject("Please Verify Your Email");
	            message.setText("Thank you for registering. Please click the link below to verify your account:\n" + verificationLink);
	            
	            mailSender.send(message);
	            logger.info("Method sendVerificationEmail: Verification email sent to {} with token {}", toEmail, token);
	        } catch (Exception e) {
	            logger.error("Method sendVerificationEmail: Failed to send verification email to {}", toEmail, e);
	        }
    	} else {
    		logger.info("Mode non-production : email not sent");
        }
    }
}