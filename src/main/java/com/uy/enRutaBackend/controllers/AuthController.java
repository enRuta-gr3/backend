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
import com.uy.enRutaBackend.icontrollers.IServiceUsuario;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private IServiceUsuario serviceUsuario ;

    @PostMapping("/registrarUsuario")
    @Operation(summary = "Registrar Usuario", description = "Permite registrar un usuario")
	public ResponseEntity<?> registrarUsuario(@RequestBody DtUsuario usuario) throws Exception {
		ResultadoOperacion<?> res = serviceUsuario.registrarUsuario(usuario);
		if(res.isSuccess()) {
			System.out.println("*REGISTRO* " + res.getMessage());
			System.out.println("*REGISTRO* " + res.getData());
			return ResponseEntity.status(HttpStatus.CREATED).body(res);
		} else {
			if(res.getMessage().contains("Error")) {
				System.out.println("*REGISTRO* " + res.getMessage());
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
			} else {
				System.out.println("*REGISTRO* " + res.getMessage());
				return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
				
			}
		}
	}
    
    @PostMapping("/iniciarSesion")
    public ResponseEntity<?> login(@RequestBody DtUsuario request) {        	
    	ResultadoOperacion<?> res = serviceUsuario.iniciarSesion(request);
    	if(res.isSuccess()) {
    		System.out.println("*LOGIN* " + res.getMessage());
    		return ResponseEntity.status(HttpStatus.OK).body(res);
        } else {
        	System.out.println("*LOGIN* " + res.getMessage());
			System.out.println("*LOGIN* " + res.getData());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }
    }

}