package com.uy.enRutaBackend.persistence;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.Localidad;
import com.uy.enRutaBackend.entities.Omnibus;

@Repository
public interface OmnibusRepository extends CrudRepository<Omnibus, Integer>{
	boolean existsByNroCoche(int nroCoche);
	
	@Query("""
			select o
			from Omnibus o
			where id_omnibus not in (select v.omnibus.id_omnibus from Viaje v)
			and o.localidad_actual = :localidad
			 """)
	List<Omnibus> omnibusSinViajeAsignado(@Param("localidad") Localidad localidad);

	@Query("SELECT o FROM Omnibus o WHERE o.fechaCreacion < :fechaLimite")
	List<Omnibus> findByFechaCreacionAnterior(@Param("fechaLimite") Date fechaLimite);

	List<Omnibus> findAll(Sort sort);

	@Query(value="""
			select * 
			from omnibus o
			where o.localidad_actual_id_localidad = :origen
			and o.activo = true
			and o.id_omnibus in (
				select v.id_omnibus 
				from Viaje v 
				where (v.fecha_partida  + v.hora_partida)  > CURRENT_TIMESTAMP 
				order by v.fecha_partida asc)
			""", nativeQuery = true)
	List<Omnibus> omnibusNoHanSalido(@Param("origen") int localidadId);
	
}
