package com.reward.points.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers(HttpMethod.GET, "/**").authenticated()
                    .requestMatchers(HttpMethod.POST, "/**").authenticated()
                    .requestMatchers(HttpMethod.PUT, "/**").authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/**").authenticated()
            )
            .httpBasic()
            .and()
            .csrf().disable();  // Disable CSRF for testing purposes. Use proper CSRF protection in production.
        return http.build();
    }

   @Bean 
   public UserDetailsService userDetailsService() {
   	UserDetails user1 = User.withDefaultPasswordEncoder() 
   			.username("rushikesh") 
   			.password("rushikesh") 
   			.roles("USER") 
   			.build(); 
   	UserDetails user2 = User.withDefaultPasswordEncoder() 
   			.username("admin") 
   			.password("admin") 
   			.roles("USER") 
   			.build(); 
   	UserDetails user3 = User.withDefaultPasswordEncoder() 
   			.username("user") 
   			.password("user") 
   			.roles("USER") 
   			.build(); 
   	
   	return new InMemoryUserDetailsManager(user1, user2, user3);
   }
    
}
