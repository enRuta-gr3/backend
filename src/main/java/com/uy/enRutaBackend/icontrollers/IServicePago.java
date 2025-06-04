package com.uy.enRutaBackend.icontrollers;

import com.uy.enRutaBackend.datatypes.DtVenta_Compra;
import com.uy.enRutaBackend.entities.Pago;
import com.uy.enRutaBackend.entities.Venta_Compra;
import com.uy.enRutaBackend.errors.ResultadoOperacion;

public interface IServicePago {

	ResultadoOperacion<?> solicitarMediosPago(DtVenta_Compra compra);
	Pago crearPago(double monto, int mpId);
	ResultadoOperacion<?> solicitarParametrosMercadoPago(DtVenta_Compra compra, Venta_Compra venta);
}
