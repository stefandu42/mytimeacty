package mytimeacty.security;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import mytimeacty.filter.JwtAuthenticationFilter;

@Configuration
public class FilterConfig {

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() {
	    return new JwtAuthenticationFilter();
	}
    
    @Bean
	public FilterRegistrationBean<JwtAuthenticationFilter> filterRegistrationBean() { 
	    
	  FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean(); 
	    
	  registrationBean.setFilter(jwtAuthenticationFilter()); 
	  registrationBean.setOrder(0);
	    
	  return registrationBean; 
	}
}
