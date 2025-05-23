package com.uy.enRutaBackend.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.Venta_Compra;

@Repository
public interface VentaCompraRepository extends CrudRepository<Venta_Compra, Integer>{

}
