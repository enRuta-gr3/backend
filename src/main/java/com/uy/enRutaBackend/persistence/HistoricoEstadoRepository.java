package com.uy.enRutaBackend.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.Historico_estado;

@Repository
public interface HistoricoEstadoRepository extends CrudRepository<Historico_estado, Integer>{

}
