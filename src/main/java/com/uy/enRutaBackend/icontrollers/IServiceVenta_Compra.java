package com.uy.enRutaBackend.icontrollers;

import java.util.List;

import com.uy.enRutaBackend.datatypes.DtPasaje;
import com.uy.enRutaBackend.datatypes.DtVentaCompraResponse;
import com.uy.enRutaBackend.datatypes.DtVenta_Compra;
import com.uy.enRutaBackend.entities.Cliente;
import com.uy.enRutaBackend.entities.Pago;
import com.uy.enRutaBackend.entities.Venta_Compra;

public interface IServiceVenta_Compra {
	DtVentaCompraResponse calcularVenta(List<DtPasaje> request);
	Venta_Compra armarVenta(DtVenta_Compra compra);
	
}
