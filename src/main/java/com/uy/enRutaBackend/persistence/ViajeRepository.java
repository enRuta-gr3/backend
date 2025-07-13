package com.uy.enRutaBackend.persistence;


import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
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
		    SELECT o
			FROM Omnibus o
			WHERE 
			    EXISTS (
			        SELECT 1 
			        FROM Viaje v_inicio
			        WHERE v_inicio.omnibus.id_omnibus = o.id_omnibus
			          AND (
			              v_inicio.fecha_llegada < :fechaPartida
			              OR (v_inicio.fecha_llegada = :fechaPartida AND v_inicio.hora_llegada <= :horaPartida)
			          )
			          AND v_inicio.localidadDestino.id_localidad = :origen
			    )
			    AND NOT EXISTS (
			        SELECT 1 
			        FROM Viaje v_conflicto
			        WHERE v_conflicto.omnibus.id_omnibus = o.id_omnibus
			          AND (
			              (v_conflicto.fecha_partida < :fechaLlegada
			               OR (v_conflicto.fecha_partida = :fechaLlegada AND v_conflicto.hora_partida < :horaLlegada))
			              AND
			              (v_conflicto.fecha_llegada > :fechaPartida
			               OR (v_conflicto.fecha_llegada = :fechaPartida AND v_conflicto.hora_llegada > :horaPartida))
			          )
			    )
		""")
	List<Omnibus> omnibusSinViajes(@Param("fechaPartida") Date fechaPartida,
		    @Param("horaPartida") Time horaPartida,
		    @Param("fechaLlegada") Date fechaLlegada,
		    @Param("horaLlegada") Time horaLlegada,
		    @Param("origen") Integer localidadOrigenId
		    );

	@Query("SELECT v FROM Viaje v WHERE EXTRACT(YEAR FROM v.fecha_partida) = :anio")
	List<Viaje> obtenerViajesPorAnio(@Param("anio") int anio);

	@Query("select v.omnibus from Viaje v group by omnibus")
	List<Omnibus> obtenerOmnibusAsignados();
	
	@Modifying
	@Query("UPDATE Viaje v SET v.estado = :estado WHERE v.id_viaje = :id")
	void actualizarEstadoViaje(@Param("id") int id, @Param("estado") EstadoViaje estado);

	@EntityGraph(attributePaths = {
		    "localidadOrigen", "localidadOrigen.departamento",
		    "localidadDestino", "localidadDestino.departamento",
		    "omnibus"
		})
		@Query("SELECT v FROM Viaje v ORDER BY v.fecha_partida ASC")
	List<Viaje> findAllOrderedByFecha();

	@Query("SELECT v FROM Viaje v where v.omnibus.id_omnibus = :idOmnibus order by v.fecha_partida DESC")
	List<Viaje> findByOmnibusOrderedByFecha(@Param("idOmnibus") int idOmnibus);


}
