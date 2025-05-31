package com.uy.enRutaBackend.persistence;

import org.springframework.data.repository.CrudRepository;

import com.uy.enRutaBackend.entities.Sesion;

public interface SesionRepository extends CrudRepository<Sesion, Integer>{

	Sesion findByAccessToken(String token);

}
