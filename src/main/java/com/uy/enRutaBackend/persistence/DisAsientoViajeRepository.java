package com.uy.enRutaBackend.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.Asiento;
import com.uy.enRutaBackend.entities.DisAsiento_Viaje;
import com.uy.enRutaBackend.entities.EstadoAsiento;
import com.uy.enRutaBackend.entities.Omnibus;
import com.uy.enRutaBackend.entities.Viaje;

@Repository
public interface DisAsientoViajeRepository extends CrudRepository<DisAsiento_Viaje, Integer>{
	Object findByAsientoAndViaje(Asiento asiento, Viaje viaje);
	int countByViajeAndEstado(Viaje viaje, EstadoAsiento libre);
}
