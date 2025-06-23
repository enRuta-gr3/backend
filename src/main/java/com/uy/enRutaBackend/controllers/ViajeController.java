package com.uy.enRutaBackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@PostMapping("/registrarViaje")
	@Operation(summary = "Dar de alta un viaje.", description = "Permite crear un viaje.")
	public ResponseEntity<?> registrarViaje(@RequestBody DtViaje viaje) {
		ResultadoOperacion<?> res = serviceViaje.RegistrarViaje(viaje);
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
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
			}
		}
	}

	@GetMapping("/listarViajes")
	@Operation(summary = "Lista los viajes existentes.", description = "Permite listar todos los viajes dados de alta en el sistema.")
	public ResponseEntity<?> listarViajes() throws NoExistenViajesException {
		ResultadoOperacion<?> res = serviceViaje.listarViajes();
		if (res != null && res.isSuccess()) {
			System.out.println("*VIAJES* " + res.getMessage());
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
	
	@GetMapping("/reasignarViaje")
	@Operation(summary = "Permite asignar un nuevo omnibus a un viaje.")
	public ResponseEntity<?> reasignarOmnibus(@RequestParam int idViaje, @RequestParam int idOmnibus) {
		ResultadoOperacion<?> resultado = serviceViaje.reasignarOmnibus(idViaje, idOmnibus);
		if (resultado.isSuccess()) {
			System.out.println("*REASIGNAR VIAJE* - " + resultado.getMessage());
			return ResponseEntity.ok(resultado);
		} else {
			System.out.println("*REASIGNAR VIAJE* - " + resultado.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado);
		}
	}
	
	@GetMapping("/listarPorOmnibus")
	@Operation(summary = "Lista los viajes asignados al omnibus indicado")
	public ResponseEntity<?> listarViajesPorOmnibus(@RequestParam int idOmnibus) {
		ResultadoOperacion<?> resultado = serviceViaje.listarViajesPorOmnibus(idOmnibus);
		if (resultado.isSuccess()) {
			System.out.println("*VIAJES DE UN OMNIBUS* - " + resultado.getMessage());
			return ResponseEntity.ok(resultado);
		} else {
			System.out.println("*VIAJES DE UN OMNIBUS* - " + resultado.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado);
		}
	}
}
