package com.uy.enRutaBackend.icontrollers;

import com.uy.enRutaBackend.datatypes.DtViaje;
import com.uy.enRutaBackend.errors.ResultadoOperacion;

public interface IServiceAsiento {
	
	ResultadoOperacion<?> listarAsientosDeOmnibus(DtViaje viaje);
}