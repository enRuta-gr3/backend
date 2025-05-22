package com.uy.enRutaBackend.icontrollers;

import com.uy.enRutaBackend.entities.Asiento;
import com.uy.enRutaBackend.errors.ResultadoOperacion;

public interface IServiceAsiento {
	
	 public ResultadoOperacion<Asiento> RegistrarAsiento(Asiento asiento);

}
