package com.uy.enRutaBackend.icontrollers;

import com.uy.enRutaBackend.datatypes.DtLocalidad;
import com.uy.enRutaBackend.entities.Localidad;

public interface IServiceLocalidad {
	
	public void RegistrarLocalidad(Localidad localidad);
	public DtLocalidad convertirLocalidadADt(Localidad localidad);

}
