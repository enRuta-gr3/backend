package com.uy.enRutaBackend.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.Historico_estado;
import com.uy.enRutaBackend.entities.Omnibus;

@Repository
public interface HistoricoEstadoRepository extends CrudRepository<Historico_estado, Integer>{
	List<Historico_estado> findByOmnibus(Omnibus omnibus);
}
