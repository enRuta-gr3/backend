package com.uy.enRutaBackend.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.Pasaje;
import com.uy.enRutaBackend.entities.Venta_Compra;
import com.uy.enRutaBackend.entities.Viaje;

@Repository
public interface PasajeRepository extends CrudRepository<Pasaje, Integer>{

	List<Pasaje> findAllByVentaCompra(Venta_Compra compra);
	List<Pasaje> findByViaje(Viaje viaje);

}
