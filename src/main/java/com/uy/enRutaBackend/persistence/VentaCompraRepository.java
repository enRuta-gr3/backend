package com.uy.enRutaBackend.persistence;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.Cliente;
import com.uy.enRutaBackend.entities.Vendedor;
import com.uy.enRutaBackend.entities.Venta_Compra;

@Repository
public interface VentaCompraRepository extends CrudRepository<Venta_Compra, Integer>{

	List<Venta_Compra> findAllByCliente(Cliente c);
	List<Venta_Compra> findAllByVendedor(Vendedor v);
	
	@Query("""
		    SELECT u.uuidAuth, u.nombres, COALESCE(AVG(p.monto), 0)
		    FROM Usuario u
		    LEFT JOIN Venta_Compra vc ON vc.cliente.uuidAuth = u.uuidAuth
		    LEFT JOIN Pago p ON vc.pago.id = p.id
		    WHERE TYPE(u) = Cliente AND u.eliminado = false
		    GROUP BY u.uuidAuth, u.nombres
		""")
		List<Object[]> obtenerPromedioImportePorClienteRaw();





}
