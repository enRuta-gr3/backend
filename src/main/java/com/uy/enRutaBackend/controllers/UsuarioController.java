package com.uy.enRutaBackend.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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

	@Autowired
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
	
	@DeleteMapping("/eliminarUsuario")
	@Operation(summary = "Eliminar usuario", description = "Marca como eliminado y borra datos sensibles")
	public ResponseEntity<?> eliminarUsuario(
	        @RequestHeader("Authorization") String token,
	        @RequestBody DtUsuario datos) {

	    ResultadoOperacion<?> res = serviceUsuario.eliminarUsuario(token, datos);

	    if (res.isSuccess()) {
	        return ResponseEntity.ok(res);
	    } else {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
	    }
	}
	
	@PostMapping("/modificarPerfil")
    @Operation(summary = "Modificar Perfil")
	public ResponseEntity<?> modificarPerfil(@RequestBody DtUsuario usuario) {
		ResultadoOperacion<?> res = serviceUsuario.modificarPerfil(usuario);

		if (res.isSuccess()) {
			System.out.println("*MODIFICACION DE PERFIL* " + res.getMessage());
			System.out.println("*MODIFICACION DE PERFIL* " + res.getData());
			return ResponseEntity.ok(res);
		} else {
			System.out.println("*MODIFICACION DE PERFIL* " + res.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
		}
	}
	
	@GetMapping("/listarUsuarios")
    @Operation(summary = "Listar usuarios")
	public ResponseEntity<?> listarUsuarios() {
		ResultadoOperacion<?> res = serviceUsuario.listarUsuarios();

		if (res.isSuccess()) {
			System.out.println("*LISTAR USUARIOS* " + res.getMessage());
			return ResponseEntity.ok(res);
		} else {
			System.out.println("*LISTAR USUARIOS* " + res.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
		}
	}
	
	@PostMapping("/listarNotificaciones")
	@Operation(summary = "Lista las notificaciones de un usuario")
	public ResponseEntity<?> listarNotificaciones(@RequestBody DtUsuario usuario) {
		ResultadoOperacion<?> res = serviceUsuario.listarNotificaciones(usuario);
		if (res.isSuccess()) {
			System.out.println("*LISTAR NOTIFICACIONES* " + res.getMessage());
			return ResponseEntity.ok(res);
		} else {
			System.out.println("*LISTAR USUARIOS* " + res.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
		}
	}
	
	@PostMapping("/verificarDescuento")
	@Operation(summary = "Permite verificar el descuento de un cliente")
	public ResponseEntity<?> verificarDescuento(@RequestBody DtUsuario usuario) {
		ResultadoOperacion<?> res = serviceUsuario.verificarDescuento(usuario);
		if (res.isSuccess()) {
			System.out.println("*VERIFICAR DESCUENTO* " + res.getMessage());
			return ResponseEntity.ok(res);
		} else {
			System.out.println("*VERIFICAR DESCUENTO* " + res.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
		}
	}
	
}
