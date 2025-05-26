package com.uy.enRutaBackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uy.enRutaBackend.datatypes.DtUsuario;
import com.uy.enRutaBackend.errors.ResultadoOperacion;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioController usuarioController ;

    @PostMapping("/registrarUsuario")
    @Operation(summary = "Registrar Usuario", description = "Permite registrar un usuario")
	public ResponseEntity<?> registrarUsuario(@RequestBody DtUsuario usuario) {
		ResultadoOperacion res = usuarioController.registrarUsuario(usuario);
		if(res.isSuccess()) {
			return ResponseEntity.status(HttpStatus.CREATED).body(res.getData());
		} else {
			if(res.getMessage().contains("Error")) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res.getData());
			} else {
				return ResponseEntity.status(HttpStatus.CONFLICT).body(res.getData());
			}
		}
	}
    
    @PostMapping("/iniciarSesion")
    public ResponseEntity<?> login(@RequestBody DtUsuario request) {
        	
    	ResultadoOperacion res = usuarioController.iniciarSesion(request);
    	if(res.isSuccess()) {
    		return ResponseEntity.status(HttpStatus.OK).body(res.getData());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res.getMessage());
        }
    }

}