package com.reward.points.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class AppConfig {
// currently doing in-memory authentication
	
	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails user1 = User.builder().username("rushikesh").password(passwordEncoder().encode("rushikesh")).roles("Admin").build();
		UserDetails user2 = User.builder().username("admin").password(passwordEncoder().encode("admin")).roles("Admin").build();
		
		return new InMemoryUserDetailsManager(user1, user2);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
		}
}
