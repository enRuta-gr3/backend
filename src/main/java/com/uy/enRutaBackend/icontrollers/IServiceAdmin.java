package com.uy.enRutaBackend.icontrollers;

import java.util.List;

import com.uy.enRutaBackend.datatypes.DtEstadisticaActividadUsuarios;
import com.uy.enRutaBackend.datatypes.DtEstadisticaUsuarios;
import com.uy.enRutaBackend.datatypes.DtPromedioClienteDTO;
import com.uy.enRutaBackend.datatypes.DtUsuario;
import com.uy.enRutaBackend.errors.ResultadoOperacion;

public interface IServiceAdmin {
	
	public ResultadoOperacion<?> eliminarUsuarioComoAdmin(String token, DtUsuario datos);
	DtEstadisticaUsuarios obtenerEstadisticasUsuarios();
	DtEstadisticaActividadUsuarios obtenerEstadisticasActividadUsuarios();
	List<DtPromedioClienteDTO> obtenerPromedioImportePorCliente();

}
