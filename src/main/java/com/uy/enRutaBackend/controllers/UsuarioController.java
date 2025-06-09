package com.uy.enRutaBackend.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uy.enRutaBackend.datatypes.DtUsuario;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.icontrollers.IServiceUsuario;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

	private final IServiceUsuario serviceUsuario;

	@Autowired
	public UsuarioController(IServiceUsuario serviceUsuario) {
		this.serviceUsuario = serviceUsuario;
	}
	
	@PostMapping("/cambiarContraseña")
	@Operation(summary = "Cambiar contraseña del usuario")
	public ResponseEntity<?> cambiarPassword(@RequestBody DtUsuario datos) {
	    ResultadoOperacion<?> res = serviceUsuario.cambiarPassword(datos);

	    if (res.isSuccess()) {
	        return ResponseEntity.ok(res);
	    } else {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
	    }
	}
	
	@PostMapping("/solicitar-recuperacion")
	public ResponseEntity<?> solicitarRecuperacion(@RequestBody Map<String, String> request) {
	    ResultadoOperacion<?> res = serviceUsuario.solicitarRecuperacion(request.get("email"));
	    return ResponseEntity.status(res.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(res);
	}

	@PostMapping("/confirmar-recuperacion")
	public ResponseEntity<?> confirmarRecuperacion(@RequestBody Map<String, String> request) {
	    ResultadoOperacion<?> res = serviceUsuario.confirmarRecuperacion(request.get("token"), request.get("nuevaPassword"));
	    return ResponseEntity.status(res.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(res);
	}

	
}
