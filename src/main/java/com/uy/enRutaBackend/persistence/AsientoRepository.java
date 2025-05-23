package com.uy.enRutaBackend.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.Asiento;

@Repository
public interface AsientoRepository extends CrudRepository<Asiento, Integer>{

}
