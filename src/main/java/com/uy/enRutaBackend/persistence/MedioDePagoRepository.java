package com.uy.enRutaBackend.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.Medio_de_Pago;

@Repository
public interface MedioDePagoRepository extends CrudRepository<Medio_de_Pago, Integer>{

}
