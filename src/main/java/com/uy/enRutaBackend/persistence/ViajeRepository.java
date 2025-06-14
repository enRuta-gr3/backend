package com.uy.enRutaBackend.persistence;


import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.EstadoViaje;
import com.uy.enRutaBackend.entities.Omnibus;
import com.uy.enRutaBackend.entities.Viaje;

@Repository
public interface ViajeRepository extends CrudRepository<Viaje, Integer>{
	List<Viaje> findByOmnibus(Omnibus omnibus);
	

	@Query("""
		    SELECT COUNT(v) > 0 FROM Viaje v
		    WHERE v.omnibus.id_omnibus = :idOmnibus
		    AND (
		        (CAST(CONCAT(v.fecha_partida, ' ', v.hora_partida) AS timestamp) < :fin)
		        AND (CAST(CONCAT(v.fecha_llegada, ' ', v.hora_llegada) AS timestamp) > :inicio)
		    )
		""")
		boolean existeViajeEntreFechas(
		    @Param("idOmnibus") int idOmnibus,
		    @Param("inicio") Timestamp inicio,
		    @Param("fin") Timestamp fin
		);

/*
	Optional<Omnibus> findByFechaPartidaAndHoraPartidaAndFechaLlegadaAndHoraLlegadaAndEstado(Date fechaPartida,
			Time horaPartida, Date fechaLlegada, Time horaLlegada, EstadoViaje abierto);
*/
	@Query("SELECT v FROM Viaje v WHERE " +
		       "v.localidadOrigen.id = :idLocalidadOrigen AND " +
		       "v.localidadDestino.id = :idLocalidadDestino AND " +
		       "v.fecha_partida = :fechaPartida AND " +
		       "v.fecha_llegada = :fechaLlegada AND " +
		       "v.hora_partida = :horaPartida AND " +
		       "v.hora_llegada = :horaLlegada AND " +
		       "v.omnibus.id = :idOmnibus AND " +
		       "v.estado = :estado")
		Optional<Viaje> mismoViaje(
		    @Param("idLocalidadOrigen") int idLocalidadOrigen,
		    @Param("idLocalidadDestino") int idLocalidadDestino,
		    @Param("fechaPartida") Date fechaPartida,
		    @Param("horaPartida") Time horaPartida,
		    @Param("fechaLlegada") Date fechaLlegada,
		    @Param("horaLlegada") Time horaLlegada,
		    @Param("idOmnibus") int idOmnibus,
		    @Param("estado") EstadoViaje estado
		);




}
