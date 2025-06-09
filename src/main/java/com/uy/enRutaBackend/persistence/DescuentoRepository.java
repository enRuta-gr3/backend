package com.uy.enRutaBackend.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.Descuento;

@Repository
public interface DescuentoRepository extends CrudRepository<Descuento, Integer>{
}
