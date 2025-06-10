package com.uy.enRutaBackend.icontrollers;

import com.uy.enRutaBackend.datatypes.DtUsuario;
import com.uy.enRutaBackend.errors.ResultadoOperacion;

public interface IServiceAdmin {
	
	public ResultadoOperacion<?> eliminarUsuarioComoAdmin(String token, DtUsuario datos);

}
