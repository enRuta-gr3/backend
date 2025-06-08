package com.uy.enRutaBackend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uy.enRutaBackend.datatypes.DtUsuario;
import com.uy.enRutaBackend.errors.ErrorCode;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.icontrollers.IServiceUsuario;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
	private final IServiceUsuario serviceUsuario;

	public UsuarioController(IServiceUsuario serviceUsuario) {
		this.serviceUsuario = serviceUsuario;
	}

	@PostMapping("/buscarPorCi")
    @Operation(summary = "Buscar cliente por CI")
	public ResponseEntity<?> buscarUsuarioPorCi(@RequestBody DtUsuario usuario) {
		ResultadoOperacion<?> res = serviceUsuario.buscarUsuarioPorCi(usuario);

		if (res.isSuccess()) {
			System.out.println("*BUSQUEDA POR CI* " + res.getMessage());
			System.out.println("*BUSQUEDA POR CI* " + res.getData());
			return ResponseEntity.ok(res);
		} else {
			if (res.getErrorCode().equals(ErrorCode.SIN_RESULTADOS)) {
				System.out.println("*BUSQUEDA POR CI* " + res.getMessage());
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(res);
			} else {
				System.out.println("*BUSQUEDA POR CI* " + res.getMessage());
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);

			}
		}

	}
}
