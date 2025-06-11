package com.uy.enRutaBackend.icontrollers;

import com.uy.enRutaBackend.datatypes.DtDepartamento;
import com.uy.enRutaBackend.errors.ResultadoOperacion;

public interface IServiceDepartamento {
	
	public ResultadoOperacion<DtDepartamento> listarDepartamentos();
}
