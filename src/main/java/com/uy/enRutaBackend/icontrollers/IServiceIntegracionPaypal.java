package com.uy.enRutaBackend.icontrollers;

import java.io.IOException;
import java.math.BigDecimal;

import com.uy.enRutaBackend.datatypes.DtPaypal;

public interface IServiceIntegracionPaypal {
	DtPaypal crearOrdenDePago(BigDecimal amount, String urlRedir, int idVenta) throws IOException;
	String capturePayment(String orderId) throws IOException;
}
