package com.uy.enRutaBackend.persistence;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.EstadoPasaje;
import com.uy.enRutaBackend.entities.Pasaje;
import com.uy.enRutaBackend.entities.Venta_Compra;
import com.uy.enRutaBackend.entities.Viaje;

@Repository
public interface PasajeRepository extends CrudRepository<Pasaje, Integer>{

	List<Pasaje> findAllByVentaCompra(Venta_Compra compra);
	
	@Query("SELECT p FROM Pasaje p WHERE p.ventaCompra.id IN :ids order by p.viaje.fecha_partida desc")
	List<Pasaje> findAllByVentaCompraIds(@Param("ids") List<Integer> ids);

	
	List<Pasaje> findByViaje(Viaje viaje);

	@Query(value = """
		    SELECT p.*
		    FROM pasaje p
		    JOIN viaje v ON p.id_viaje = v.id_viaje
		    JOIN venta_compra vc ON p.id_venta_compra = vc.id_venta
		    WHERE v.estado = :estado
		      AND (v.fecha_partida::timestamp + v.hora_partida) BETWEEN :desde AND :hasta
		      AND vc.estado = 'COMPLETADA'
		      AND p.estado_pasaje = 'VIGENTE'
		""", nativeQuery = true)
		List<Pasaje> findPasajesDeViajesCercanos(
		    @Param("estado") String estado,
		    @Param("desde") Timestamp desde,
		    @Param("hasta") Timestamp hasta
		);
	
	@Query("SELECT p FROM Pasaje p WHERE p.ventaCompra = :compra AND p.estadoPasaje = :estado order by p.viaje.fecha_partida DESC")
	List<Pasaje> findAllByVentaCompraAndEstadoPasaje(@Param("compra") Venta_Compra compra, @Param("estado") EstadoPasaje estado);
	
	@Query("SELECT p FROM Pasaje p WHERE EXTRACT(YEAR FROM p.fechaVenta) = :anio AND p.fechaDevolucion is null")
	List<Pasaje> obtenerVendidosPorAnio(@Param("anio")int anio);
	
	@Query("SELECT p FROM Pasaje p WHERE EXTRACT(YEAR FROM p.fechaDevolucion) = :anio")
	List<Pasaje> obtenerDevueltosPorAnio(int anio);
	
	@Query("SELECT p FROM Pasaje p WHERE p.viaje = :viaje order by p.viaje.fecha_partida desc")
	List<Pasaje> findByViajeOrderedByFecha(@Param("viaje") Viaje viaje);

}
