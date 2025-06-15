package com.uy.enRutaBackend.icontrollers;

import java.util.List;

import com.uy.enRutaBackend.datatypes.DtOmnibus;
import com.uy.enRutaBackend.datatypes.DtPasaje;
import com.uy.enRutaBackend.entities.Pasaje;
import com.uy.enRutaBackend.entities.Venta_Compra;
import com.uy.enRutaBackend.errors.ResultadoOperacion;

public interface IServicePasaje {
	
	public void RegistrarPasajes(List<Pasaje> pasajes);
	public List<Pasaje> CrearPasajes(List<DtOmnibus> omnibusDTOs, Venta_Compra venta);
	public DtPasaje entityToDt(Pasaje pasaje);
	public ResultadoOperacion<?> solicitarHistorial(List<Venta_Compra> comprasUsuario);
}
