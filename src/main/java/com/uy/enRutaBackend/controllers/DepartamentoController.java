package com.uy.enRutaBackend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uy.enRutaBackend.datatypes.DtDepartamento;
import com.uy.enRutaBackend.errors.ErrorCode;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.icontrollers.IServiceDepartamento;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/departamentos")
public class DepartamentoController {

	private final IServiceDepartamento serviceDepto;

	public DepartamentoController(IServiceDepartamento serviceDepto) {
		this.serviceDepto = serviceDepto;
	}
	
	@GetMapping("/listarDepartamentos")
	@Operation(summary = "Lista los departamentos.", description = "Permite listar todos los departamentos.")
	public ResponseEntity<?> listarDepartamentos() {
		ResultadoOperacion<DtDepartamento> res = serviceDepto.listarDepartamentos();
		if (res != null && res.isSuccess()) {
			System.out.println("*DEPARTAMENTOS - listar* " + res.getMessage());
			System.out.println("*DEPARTAMENTOS - listar* " + res.getData());
			return ResponseEntity.ok(res);
		} else {
			if (res.getErrorCode() == ErrorCode.LISTA_VACIA) {
				System.out.println("*DEPARTAMENTOS - listar* " + res.getMessage());
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(res);
			} else {
				System.out.println("*DEPARTAMENTOS - listar* " + res.getMessage());
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
			}
		}
	}
}
