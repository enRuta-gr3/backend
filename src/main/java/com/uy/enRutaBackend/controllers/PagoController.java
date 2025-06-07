package com.uy.enRutaBackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uy.enRutaBackend.datatypes.DtVenta_Compra;
import com.uy.enRutaBackend.entities.Venta_Compra;
import com.uy.enRutaBackend.errors.ErrorCode;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.icontrollers.IServicePago;
import com.uy.enRutaBackend.icontrollers.IServiceVenta_Compra;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

	private final IServicePago servicePago;
	private final IServiceVenta_Compra serviceVenta;

	@Autowired
	public PagoController(IServicePago servicePago, IServiceVenta_Compra serviceVenta) {
		this.servicePago = servicePago;
		this.serviceVenta = serviceVenta;
	}
	
	@PostMapping("/solicitarMediosDePago")
	@Operation(summary = "Devuelve los medios de pago habilitados al usuario logueado")
	public ResponseEntity<?> solicitarMediosPago (@RequestBody DtVenta_Compra compra) {
		ResultadoOperacion<?> res = servicePago.solicitarMediosPago(compra);
		if(res.isSuccess()) {
			System.out.println("*PAGOS* - Se envia medio de pago: " + res.getData());
			return ResponseEntity.ok(res);			
		} else if(res.getErrorCode().equals(ErrorCode.REQUEST_INVALIDO)) {
			System.out.println("*PAGOS* - No se encontraron medios de pago.");
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(res);
		} else {
			System.out.println("*PAGOS* - Ocurrió un error buscando medios de pago.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
		}
	}
	
	@PostMapping("/solicitarParametrosPago")
	@Operation(summary = "Retorna parámetros para ejecucion del pago.")
	public ResponseEntity<?> solicitarParametrosPago(@RequestBody DtVenta_Compra compra) {
		Venta_Compra venta = serviceVenta.armarVenta(compra);
		ResultadoOperacion<?> res = servicePago.solicitarParametrosPago(compra, venta);
		if(res.isSuccess()) {
			System.out.println("*PAGOS* - Parametros para crear pago enviados.");
			return ResponseEntity.ok(res);			
		} else {
			System.out.println("*PAGOS* - Error enviando datos para crear pago.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
		}
	}
	
}
