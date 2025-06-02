package com.uy.enRutaBackend.icontrollers;

import java.util.List;

import com.uy.enRutaBackend.datatypes.DtOmnibus;
import com.uy.enRutaBackend.errors.ResultadoOperacion;

public interface IServiceOmnibus {
	
	ResultadoOperacion<DtOmnibus> registrarOmnibus(DtOmnibus dto);
	ResultadoOperacion<List<DtOmnibus>> listarOmnibus();

}
