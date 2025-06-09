package com.uy.enRutaBackend.icontrollers;

import com.uy.enRutaBackend.datatypes.DtSesion;
import com.uy.enRutaBackend.datatypes.DtUsuario;

public interface IServiceSesion {
	public DtSesion crearSesion(DtUsuario usuario, String token) throws Exception;
}
