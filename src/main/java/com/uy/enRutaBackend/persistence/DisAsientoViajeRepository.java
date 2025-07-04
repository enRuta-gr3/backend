package com.uy.enRutaBackend.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.Asiento;
import com.uy.enRutaBackend.entities.DisAsiento_Viaje;
import com.uy.enRutaBackend.entities.EstadoAsiento;
import com.uy.enRutaBackend.entities.Viaje;

@Repository
public interface DisAsientoViajeRepository extends CrudRepository<DisAsiento_Viaje, Integer>{
	DisAsiento_Viaje findByAsientoAndViajeAndEstadoIn(Asiento asiento, Viaje viaje, List<EstadoAsiento> estados);
	int countByViajeAndEstado(Viaje viaje, EstadoAsiento libre);
	List<DisAsiento_Viaje> findByViajeAndIdBloqueoAndEstado(Viaje viaje, String idBloqueo, EstadoAsiento libre);
	List<DisAsiento_Viaje> findByViaje(Viaje viaje);
	List<DisAsiento_Viaje> findByViajeAndEstadoIn(Viaje viaje, List<EstadoAsiento> estados);
	List<DisAsiento_Viaje> findByEstado(EstadoAsiento estado);
	List<DisAsiento_Viaje> findByEstadoAndIdBloqueo(EstadoAsiento estado, String idBloqueo);
	DisAsiento_Viaje findByAsientoAndViajeAndEstado(Asiento asiento, Viaje viaje, EstadoAsiento estado);
	
}
