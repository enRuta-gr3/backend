package com.uy.enRutaBackend.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.Localidad;

@Repository
public interface LocalidadRepository extends CrudRepository<Localidad, Integer>{

}
