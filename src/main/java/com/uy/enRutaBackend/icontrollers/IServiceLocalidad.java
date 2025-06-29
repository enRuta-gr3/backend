package com.uy.enRutaBackend.icontrollers;

import java.util.List;

import com.uy.enRutaBackend.datatypes.DtLocalidad;
import com.uy.enRutaBackend.datatypes.DtLocalidadCargaMasiva;
import com.uy.enRutaBackend.datatypes.DtResultadoCargaMasiva;
import com.uy.enRutaBackend.errors.ResultadoOperacion;

public interface IServiceLocalidad {
	
	public ResultadoOperacion<?> RegistrarLocalidad(DtLocalidad localidadDt);
	public ResultadoOperacion<?> listarLocalidades();
	public DtResultadoCargaMasiva procesarLocalidades(List<DtLocalidadCargaMasiva> leidosCsv);

}
