package com.uy.enRutaBackend.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uy.enRutaBackend.datatypes.DtUsuario;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.icontrollers.IServiceAdmin;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final IServiceAdmin serviceAdmin;

    public AdminController(IServiceAdmin serviceAdmin) {
        this.serviceAdmin = serviceAdmin;
    }

    @DeleteMapping("/eliminarUsuario")
    @Operation(summary = "Eliminar usuario como administrador", description = "Permite a un admin eliminar cualquier usuario")
    public ResponseEntity<?> eliminarUsuarioAdmin(
            @RequestHeader("Authorization") String token,
            @RequestBody DtUsuario datos) {
        
        ResultadoOperacion<?> res = serviceAdmin.eliminarUsuarioComoAdmin(token, datos);

        if (res.isSuccess()) {
            return ResponseEntity.ok(res);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(res);
        }
    }
}