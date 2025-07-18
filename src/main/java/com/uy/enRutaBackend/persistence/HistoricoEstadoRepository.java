package com.uy.enRutaBackend.persistence;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.Historico_estado;
import com.uy.enRutaBackend.entities.Omnibus;

@Repository
public interface HistoricoEstadoRepository extends CrudRepository<Historico_estado, Integer>{
	List<Historico_estado> findByOmnibus(Omnibus omnibus);
	Historico_estado findTopByOmnibusOrderByFechaInicioDesc(Omnibus omnibus);
    Optional<Historico_estado> findTopByOmnibusAndActivoOrderByFechaInicioDesc(Omnibus omnibus, boolean activo);
    Optional<Historico_estado> findByOmnibusAndActivoAndFechaInicio(Omnibus omnibus, boolean activo, Date fechaInicio);
	@Query("select he from Historico_estado he where activo = :activo")
    List<Historico_estado> findByActivo(@Param("activo") boolean activo);


}


