package com.uy.enRutaBackend.icontrollers;

import com.uy.enRutaBackend.datatypes.DtViaje;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.exceptions.NoExistenViajesException;

public interface IServiceViaje {

	ResultadoOperacion<?> RegistrarViaje(DtViaje viajeDt);
	ResultadoOperacion<?> listarViajes() throws NoExistenViajesException;
}
