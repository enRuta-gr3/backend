package com.uy.enRutaBackend.persistence;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.EstadoViaje;
import com.uy.enRutaBackend.entities.Omnibus;
import com.uy.enRutaBackend.entities.Viaje;

@Repository
public interface ViajeRepository extends CrudRepository<Viaje, Integer>{
	List<Viaje> findByOmnibus(Omnibus omnibus);
	Optional<Viaje> findByFechaPartidaAndHoraPartidaAndFechaLlegadaAndHoraLlegadaAndEstado(
	        Date fechaPartida, 
	        Time horaPartida, 
	        Date fechaLlegada, 
	        Time horaLlegada,
	        EstadoViaje estado
	    );
}
