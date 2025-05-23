package com.uy.enRutaBackend.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.DisAsiento_Viaje;

@Repository
public interface DisAsientoViajeRepository extends CrudRepository<DisAsiento_Viaje, Integer>{

}
