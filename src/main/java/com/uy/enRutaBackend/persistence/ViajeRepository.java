package com.uy.enRutaBackend.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.Omnibus;
import com.uy.enRutaBackend.entities.Viaje;

@Repository
public interface ViajeRepository extends CrudRepository<Viaje, Integer>{
	List<Viaje> findByOmnibus(Omnibus omnibus);
}
