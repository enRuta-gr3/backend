package com.uy.enRutaBackend.icontrollers;

import java.util.List;

import com.uy.enRutaBackend.datatypes.DtHistoricoEstado;
import com.uy.enRutaBackend.datatypes.DtOmnibus;
import com.uy.enRutaBackend.datatypes.DtOmnibusCargaMasiva;
import com.uy.enRutaBackend.datatypes.DtResultadoCargaMasiva;
import com.uy.enRutaBackend.errors.ResultadoOperacion;

public interface IServiceOmnibus {
	
	ResultadoOperacion<DtOmnibus> registrarOmnibus(DtOmnibus dto);
	ResultadoOperacion<List<DtOmnibus>> listarOmnibus();
	public ResultadoOperacion<?> cambiarEstadoOmnibus(DtHistoricoEstado dto);
	DtResultadoCargaMasiva procesarOmnibus(List<DtOmnibusCargaMasiva> leidosCsv);
	ResultadoOperacion<?> buscarOmnibusDisponibles(int idViaje);
	ResultadoOperacion<?> calcularPorcentajeAsignados();
	
}
