package com.uy.enRutaBackend.icontrollers;

import java.util.List;

import com.uy.enRutaBackend.datatypes.DtPasaje;
import com.uy.enRutaBackend.datatypes.DtVentaCompraResponse;

public interface IServiceVenta_Compra {
	DtVentaCompraResponse calcularVenta(List<DtPasaje> request);

}
