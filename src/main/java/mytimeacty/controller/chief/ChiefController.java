package mytimeacty.controller.chief;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chief")
public class ChiefController {

    @GetMapping("/dashboard")
    public ResponseEntity<String> getChiefDashboard() {
        return ResponseEntity.ok("Chief Dashboard");
    }

    @PostMapping("/assign")
    public ResponseEntity<String> assignChiefTask() {
        return ResponseEntity.ok("Chief Task Assigned");
    }
}