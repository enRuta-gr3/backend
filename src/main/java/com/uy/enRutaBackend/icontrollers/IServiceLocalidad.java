package com.uy.enRutaBackend.icontrollers;

import com.uy.enRutaBackend.datatypes.DtLocalidad;
import com.uy.enRutaBackend.entities.Localidad;
import com.uy.enRutaBackend.errors.ResultadoOperacion;

public interface IServiceLocalidad {
	
	public ResultadoOperacion<?> RegistrarLocalidad(DtLocalidad localidadDt);

	public ResultadoOperacion<?> listarLocalidades();

}
