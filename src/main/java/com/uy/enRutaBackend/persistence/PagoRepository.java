package com.uy.enRutaBackend.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.Pago;

@Repository
public interface PagoRepository extends CrudRepository<Pago, Integer>{

}
