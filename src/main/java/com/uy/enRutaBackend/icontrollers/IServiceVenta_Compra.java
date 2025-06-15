package com.uy.enRutaBackend.icontrollers;

import java.util.List;

import com.uy.enRutaBackend.datatypes.DtPasaje;
import com.uy.enRutaBackend.datatypes.DtPaypal;
import com.uy.enRutaBackend.datatypes.DtUsuario;
import com.uy.enRutaBackend.datatypes.DtVentaCompraResponse;
import com.uy.enRutaBackend.datatypes.DtVenta_Compra;
import com.uy.enRutaBackend.entities.Cliente;
import com.uy.enRutaBackend.entities.Pago;
import com.uy.enRutaBackend.entities.Venta_Compra;
import com.uy.enRutaBackend.errors.ResultadoOperacion;

public interface IServiceVenta_Compra {
	DtVentaCompraResponse calcularVenta(List<DtPasaje> request);
	Venta_Compra armarVenta(DtVenta_Compra compra);
	ResultadoOperacion<?> finalizarVenta(DtVenta_Compra compra);
	ResultadoOperacion<?> finalizarVentaPayPal(DtPaypal paypalDt);
	List<Venta_Compra> listarVentas(DtUsuario usuario);
}
