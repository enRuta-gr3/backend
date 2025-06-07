package com.uy.enRutaBackend.icontrollers;

import java.util.List;

import com.uy.enRutaBackend.datatypes.DtDisAsiento;
import com.uy.enRutaBackend.datatypes.DtViaje;
import com.uy.enRutaBackend.errors.ResultadoOperacion;

public interface IServiceAsiento {	
	ResultadoOperacion<?> listarAsientosDeOmnibus(DtViaje viaje);
	ResultadoOperacion<?> cambiarEstadoDisponibilidad(List<DtDisAsiento> asientos);
	void desbloquearPorTiempo();
}