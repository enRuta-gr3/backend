package com.uy.enRutaBackend.persistence;

import org.springframework.data.repository.CrudRepository;

import com.uy.enRutaBackend.entities.Sesion;
import com.uy.enRutaBackend.entities.Usuario;

public interface SesionRepository extends CrudRepository<Sesion, Integer>{

	Sesion findByAccessToken(String token);
	Sesion findByUsuario(Usuario usuario);
	Sesion findByUsuarioAndAccessToken(Usuario usu, String token);

}
