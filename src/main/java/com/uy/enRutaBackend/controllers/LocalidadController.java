package com.uy.enRutaBackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uy.enRutaBackend.datatypes.DtLocalidad;
import com.uy.enRutaBackend.errors.ErrorCode;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.exceptions.NoExistenViajesException;
import com.uy.enRutaBackend.icontrollers.IServiceLocalidad;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/localidades")
public class LocalidadController {
	
	private final IServiceLocalidad localidadService;

	@Autowired
	public LocalidadController(IServiceLocalidad localidadService) {
		this.localidadService = localidadService;
	}

	@PostMapping("/registrarLocalidad")
	@Operation(summary = "Dar de alta una localidad.", description = "Permite crear una localidad.")
	public ResponseEntity<?> registrarLocalidad(@RequestBody DtLocalidad localidad) {
		ResultadoOperacion<?> res = localidadService.RegistrarLocalidad(localidad);
		if(res.isSuccess()) {
			System.out.println("*LOCALIDADES* " + res.getMessage());
//			System.out.println("*LOCALIDADES* " + res.getData());
			return ResponseEntity.status(HttpStatus.CREATED).body(res);
		} else {
			if(res.getErrorCode() == ErrorCode.ERROR_DE_CREACION) {
				System.out.println("*LOCALIDADES* " + res.getMessage());
				return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
			} else {
				System.out.println("*LOCALIDADES* " + res.getMessage());
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
			}
		}
	}
	
	@GetMapping("/listarLocalidades")
	@Operation(summary = "Lista las localidades existentes.", description = "Permite listar todas las localidades dadas de alta en el sistema.")
	public ResponseEntity<?> listarLocalidades() throws NoExistenViajesException {
		ResultadoOperacion<?> res = localidadService.listarLocalidades();
		if (res != null && res.isSuccess()) {
			System.out.println("*LOCALIDADES* " + res.getMessage());
			System.out.println("*LOCALIDADES* " + res.getData());
			return ResponseEntity.ok(res);
		} else {
			if (res.getErrorCode() == ErrorCode.LISTA_VACIA) {
				System.out.println("*LOCALIDADES* " + res.getMessage());
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(res);
			} else {
				System.out.println("*LOCALIDADES* " + res.getMessage());
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
			}
		}
	}
	
}
