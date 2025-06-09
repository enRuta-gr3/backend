package com.uy.enRutaBackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uy.enRutaBackend.datatypes.DtVenta_Compra;
import com.uy.enRutaBackend.errors.ErrorCode;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.icontrollers.IServicePago;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

	private final IServicePago servicePago;

	@Autowired
	public PagoController(IServicePago servicePago) {
		this.servicePago = servicePago;
	}
	
	@PostMapping("/solicitarMediosDePago")
	@Operation(summary = "Devuelve los medios de pago habilitados al usuario logueado")
	public ResponseEntity<?> solicitarMediosPago (@RequestBody DtVenta_Compra compra) {
		ResultadoOperacion<?> res = servicePago.solicitarMediosPago(compra);
		if(res.isSuccess()) {
			return ResponseEntity.ok(res);			
		} else if(res.getErrorCode().equals(ErrorCode.REQUEST_INVALIDO)) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(res);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
		}
	}
}
