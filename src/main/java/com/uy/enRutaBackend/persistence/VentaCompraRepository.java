package com.uy.enRutaBackend.persistence;


import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.Cliente;
import com.uy.enRutaBackend.entities.Vendedor;
import com.uy.enRutaBackend.entities.Venta_Compra;

@Repository
public interface VentaCompraRepository extends CrudRepository<Venta_Compra, Integer>{

	List<Venta_Compra> findAllByCliente(Cliente c);
	List<Venta_Compra> findAllByVendedor(Vendedor v);
}
