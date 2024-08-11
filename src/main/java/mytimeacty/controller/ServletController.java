package mytimeacty.controller;

import org.springframework.web.bind.annotation.GetMapping; 
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.RestController; 
  
@RestController
@RequestMapping("/api") 
public class ServletController { 
    
      //Get-Request Mapping 
    @GetMapping
    public String hello(){ 
        return "This is a sample API for testing Servlet Filter - Spring Boot!"; 
    } 
}