package mytimeacty.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import mytimeacty.filter.JwtAuthenticationFilter;

@Configuration
public class FilterConfig {

    @Bean
    JwtAuthenticationFilter myFilter() {
	    return new JwtAuthenticationFilter();
	}
    
    @Bean
	public FilterRegistrationBean<JwtAuthenticationFilter> filterRegistrationBean() { 
	    
	  FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean(); 
	    
	  registrationBean.setFilter(myFilter()); 
	  registrationBean.setOrder(0);
	    
	  return registrationBean; 
	}
}
