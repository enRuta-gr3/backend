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
import com.uy.enRutaBackend.entities.Localidad;
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


	@Query("SELECT v.localidadOrigen.nombre, v.localidadOrigen.departamento.nombre, v.fecha_partida, COUNT(v) as cantidad "
			+ "FROM Viaje v "
			+ "GROUP BY localidadOrigen.nombre, v.localidadOrigen.departamento.nombre, v.fecha_partida")
	List<Object[]> contarViajes();


	@Query(value = """
		    SELECT * FROM viaje 
		    WHERE estado = :estado
		    AND (fecha_partida + hora_partida) BETWEEN :desde AND :hasta
		""", nativeQuery = true)
		List<Viaje> findViajesProximosPorEstado(
		    @Param("estado") String estado,
		    @Param("desde") Timestamp desde,
		    @Param("hasta") Timestamp hasta
		);

	@Query("""
		    SELECT v.omnibus FROM Viaje v
		    WHERE ((v.fecha_llegada = :fechaPartida AND v.hora_llegada <= :horaPartida)
		       OR (v.fecha_llegada < :fechaPartida) AND v.localidadDestino = :origen)
		       OR ((v.fecha_partida = :fechaLlegada AND v.hora_partida >= :horaLlegada)
		       OR (v.fecha_partida > :fechaLlegada) AND v.localidadOrigen = :destino)
		""")
	List<Omnibus> omnibusSinViajes(@Param("fechaPartida") Date fechaPartida,
		    @Param("horaPartida") Time horaPartida,
		    @Param("fechaLlegada") Date fechaLlegada,
		    @Param("horaLlegada") Time horaLlegada,
		    @Param("origen") Localidad localidadOrigen,
		    @Param("destino") Localidad localidadDestino
		    );

	@Query("SELECT v FROM Viaje v WHERE EXTRACT(YEAR FROM v.fecha_partida) = :anio")
	List<Viaje> obtenerViajesPorAnio(@Param("anio") int anio);

	@Query("select v.omnibus from Viaje v group by omnibus")
	List<Omnibus> obtenerOmnibusAsignados();
}
