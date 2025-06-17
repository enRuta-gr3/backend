package com.uy.enRutaBackend.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.Localidad;

@Repository
public interface LocalidadRepository extends CrudRepository<Localidad, Integer>{

	List<Localidad> findByDepartamentoIdDepartamento(int idDepartamento);

	Localidad findByDepartamentoNombreAndNombre(String nombreDepartamento, String nombreLocalidad);

}
