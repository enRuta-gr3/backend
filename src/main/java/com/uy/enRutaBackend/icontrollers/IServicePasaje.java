package com.uy.enRutaBackend.icontrollers;

import java.util.List;
import java.util.Optional;

import com.uy.enRutaBackend.datatypes.DtOmnibus;
import com.uy.enRutaBackend.entities.Pasaje;
import com.uy.enRutaBackend.entities.Venta_Compra;

public interface IServicePasaje {
	
	public void RegistrarPasajes(List<Pasaje> pasajes);
	public void CrearPasajes(List<DtOmnibus> omnibusDTOs, Venta_Compra venta);
}
