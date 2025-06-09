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
						.requestMatchers("/api/auth/iniciarSesion").permitAll()
						.requestMatchers("/api/auth/registrarUsuario").permitAll()
						.requestMatchers("/api/departamentos/listarDepartamentos").permitAll()
						.requestMatchers("/api/localidades/registrarLocalidad").permitAll()
						.requestMatchers("/api/localidades/listarLocalidades").permitAll()
						.requestMatchers("/api/viajes/registrarViaje").permitAll()
						.requestMatchers("/api/viajes/listarViajes").permitAll()
						.requestMatchers("/api/omnibus/registrar").permitAll()
						.requestMatchers("/api/omnibus/listar").permitAll()
						.requestMatchers("/api/asientos/listarAsientos").permitAll()
						.requestMatchers("/api/asientos/cambiarEstado").permitAll()
						.requestMatchers("/api/venta/calcularVenta").permitAll()
						.requestMatchers("/api/pagos/solicitarMediosDePago").permitAll()
						.requestMatchers("/api/pagos/solicitarParametrosPago").permitAll()
						.requestMatchers("/api/venta/confirmarVenta").permitAll()
						.requestMatchers("/api/venta/confirmarVentaPaypal").permitAll()
						.requestMatchers("/api/usuarios/buscarPorCi").permitAll()
						.anyRequest().authenticated()
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
