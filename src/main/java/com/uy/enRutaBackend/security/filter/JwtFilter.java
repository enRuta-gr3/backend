package com.uy.enRutaBackend.security.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.uy.enRutaBackend.security.jwt.JwtManager;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class JwtFilter extends OncePerRequestFilter {
	    
	    @Autowired
	    private JwtManager jwtManager;

	    @Override
	    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
	            throws ServletException, IOException {

	        String token = request.getHeader("Authorization");

	        if (token != null && token.startsWith("Bearer ")) {
	            Claims claims = jwtManager.validateToken(token);
	            if (claims != null) {
	                String email = claims.get("email", String.class); // Extraer email del JWT
	                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

	                UsernamePasswordAuthenticationToken authToken =
	                    new UsernamePasswordAuthenticationToken(email, null, authorities);

	                SecurityContextHolder.getContext().setAuthentication(authToken);
	            }


	        }

	        chain.doFilter(request, response);
	    }
	}

