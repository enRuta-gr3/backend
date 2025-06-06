package com.uy.enRutaBackend.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.Omnibus;

@Repository
public interface OmnibusRepository extends CrudRepository<Omnibus, Integer>{
	boolean existsByNroCoche(int nroCoche);
	
}
