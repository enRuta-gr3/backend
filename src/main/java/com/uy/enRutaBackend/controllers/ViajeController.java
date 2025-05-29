package com.uy.enRutaBackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uy.enRutaBackend.datatypes.DtViaje;
import com.uy.enRutaBackend.errors.ErrorCode;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.exceptions.NoExistenViajesException;
import com.uy.enRutaBackend.icontrollers.IServiceViaje;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/viajes")
public class ViajeController {
	
	private final IServiceViaje serviceViaje;
	
	@Autowired
	public ViajeController(IServiceViaje serviceViaje) {
		this.serviceViaje = serviceViaje;
	}
	
	public ResponseEntity<?> registrarViaje(@RequestBody DtViaje viaje) {
		ResultadoOperacion res = serviceViaje.RegistrarViaje(viaje);
		if(res.isSuccess()) {
			System.out.println("*VIAJES* " + res.getMessage());
			System.out.println("*VIAJES* " + res.getData());
			return ResponseEntity.status(HttpStatus.CREATED).body(res);
		} else {
			if(res.getErrorCode() == ErrorCode.ERROR_DE_CREACION) {
				System.out.println("*VIAJES* " + res.getMessage());
				return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
			} else {
				System.out.println("*VIAJES* " + res.getMessage());
				return ResponseEntity.status(HttpStatus.CONFLICT).body(res.getMessage());
			}
		}
	}

	@GetMapping("/listarViajes")
	@Operation(summary = "Lista los viajes existentes.", description = "Permite listar todos los viajes dados de alta en el sistema.")
	public ResponseEntity<?> listarViajes() throws NoExistenViajesException {
		ResultadoOperacion res = serviceViaje.listarViajes();
		if (res != null && res.isSuccess()) {
			System.out.println("*VIAJES* " + res.getMessage());
			System.out.println("*VIAJES* " + res.getData());
			return ResponseEntity.ok(res);
		} else {
			if (res.getErrorCode() == ErrorCode.LISTA_VACIA) {
				System.out.println("*VIAJES* " + res.getMessage());
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(res);
			} else {
				System.out.println("*VIAJES* " + res.getMessage());
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
			}
		}
	}
}
