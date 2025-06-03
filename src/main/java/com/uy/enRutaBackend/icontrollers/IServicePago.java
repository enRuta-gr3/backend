package com.uy.enRutaBackend.icontrollers;

import com.uy.enRutaBackend.datatypes.DtVenta_Compra;
import com.uy.enRutaBackend.errors.ResultadoOperacion;

public interface IServicePago {

	ResultadoOperacion<?> solicitarMediosPago(DtVenta_Compra compra);
}
