package com.uy.enRutaBackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
	@Bean
	PasswordEncoder pswEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll().requestMatchers("/auth-controller/login").permitAll().requestMatchers("/public/**").permitAll()
						.requestMatchers("/api/auth/iniciarSesion").permitAll().requestMatchers("/api/auth/registrarUsuario").permitAll().anyRequest().authenticated() 
				);
		return http.build();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
	    return new InMemoryUserDetailsManager(
	        User.withUsername("admin")
	            .password("{noop}admin") 
	            .roles("USER")
	            .build()
	    );
	}

}
