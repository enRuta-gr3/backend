package com.uy.enRutaBackend.icontrollers;

import java.util.List;

import com.uy.enRutaBackend.datatypes.DtPasaje;
import com.uy.enRutaBackend.errors.ResultadoOperacion;

public interface IServiceVendedor {
	public ResultadoOperacion<?> devolverPasajes(List<DtPasaje> pasajes);

}
