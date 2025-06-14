package com.uy.enRutaBackend.persistence;


import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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


}
