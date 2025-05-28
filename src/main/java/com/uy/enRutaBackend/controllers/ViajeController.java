package com.uy.enRutaBackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

	@GetMapping("/listarViajes")
	@Operation(summary = "Lista los viajes existentes.", description = "Permite listar todos los viajes dados de alta en el sistema.")
	public ResponseEntity<?> listarViajes() {
		try {
			ResultadoOperacion res = serviceViaje.listarViajes();
			if (res != null && res.isSuccess()) {
				System.out.println("*VIAJES* " + res.getMessage());
				System.out.println("*VIAJES* " + res.getData());
				return ResponseEntity.status(HttpStatus.OK).body(res.getData());
			}
		} catch (Exception e) {
			if (e instanceof NoExistenViajesException) {
				System.out.println("*VIAJES* " + e.getMessage());
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
			} else {
				System.out.println("*VIAJES* " + e.getMessage());
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
			}
		}
		return null;
	}
}
