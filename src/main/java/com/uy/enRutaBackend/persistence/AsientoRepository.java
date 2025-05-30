package com.uy.enRutaBackend.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.Asiento;
import com.uy.enRutaBackend.entities.Omnibus;

@Repository
public interface AsientoRepository extends CrudRepository<Asiento, Integer>{
	List<Asiento> findByOmnibus(Omnibus omnibus);
}