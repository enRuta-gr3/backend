package com.uy.enRutaBackend.icontrollers;

import java.util.List;
import java.util.UUID;

import com.uy.enRutaBackend.datatypes.DtDisAsiento;
import com.uy.enRutaBackend.datatypes.DtViaje;
import com.uy.enRutaBackend.entities.DisAsiento_Viaje;
import com.uy.enRutaBackend.entities.EstadoAsiento;
import com.uy.enRutaBackend.errors.ResultadoOperacion;

public interface IServiceAsiento {	
	ResultadoOperacion<?> listarAsientosDeOmnibus(DtViaje viaje);
	ResultadoOperacion<?> cambiarEstadoDisponibilidad(List<DtDisAsiento> asientos);
	void desbloquearPorTiempo();
	List<DisAsiento_Viaje> cambiarEstadoPorVenta(UUID uuidAuth, EstadoAsiento estado) throws Exception;
	void marcarReasignado(DisAsiento_Viaje asiento);
}