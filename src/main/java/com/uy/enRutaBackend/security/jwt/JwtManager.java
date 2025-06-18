package com.uy.enRutaBackend.security.jwt;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.uy.enRutaBackend.entities.Cliente;
import com.uy.enRutaBackend.entities.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtManager {
	
	public String generateToken(Usuario usuario) {
		if(usuario.getEmail() != null && usuario instanceof Cliente) {
		return Jwts.builder().setSubject(usuario.getUuidAuth().toString()).claim("email", usuario.getEmail())
				.claim("cedula", usuario.getCi()).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 86400000)) // valido por 1 dia. pasar a propertie
				.signWith(Jwts.SIG.HS256.key().build()).compact();
		} else {
			return Jwts.builder().setSubject(usuario.getUuidAuth().toString())
					.claim("cedula", usuario.getCi()).setIssuedAt(new Date())
					.setExpiration(new Date(System.currentTimeMillis() + 86400000)) // valido por 1 dia. pasar a propertie
					.signWith(Jwts.SIG.HS256.key().build()).compact();
		}
	}

	public Claims validateToken(String token) {
		try {
			return Jwts.parser().verifyWith(Jwts.SIG.HS256.key().build()).build().parseClaimsJws(token).getBody();
		} catch (Exception e) {
			throw new RuntimeException("Token inválido o expirado");
		}
	}

}
