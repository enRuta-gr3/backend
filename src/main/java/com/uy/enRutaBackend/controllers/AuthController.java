package com.uy.enRutaBackend.controllers;

import com.uy.enRutaBackend.datatypes.usuarioDTO;
import com.uy.enRutaBackend.icontrollers.IServiceUsuario;

import io.swagger.v3.oas.annotations.Operation;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private IServiceUsuario controladorUsuario ;

    @PostMapping("/registrar-verificado")
    @Operation(summary = "Registrar Usuario", description = "Permite registrar un usuario")
    public String registrarUsuarioVerificado(@RequestBody usuarioDTO usuario) {
        controladorUsuario.registrarUsuario(usuario);
        return "Intento de registro procesado.";
    }
    
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody usuarioDTO request) {
        	
        JSONObject result = controladorUsuario.login(request.getEmail(), request.getContraseña());
        if (result != null) {
            return ResponseEntity.ok(result.toString());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

}