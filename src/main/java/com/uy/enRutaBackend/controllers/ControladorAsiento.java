package com.uy.enRutaBackend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uy.enRutaBackend.entities.Asiento;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.icontrollers.IServiceAsiento;

@RestController
@RequestMapping("/api/asientos")
public class ControladorAsiento {

    private final IServiceAsiento serviceAsiento;

    public ControladorAsiento(IServiceAsiento serviceAsiento) {
        this.serviceAsiento = serviceAsiento;
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarAsiento(@org.springframework.web.bind.annotation.RequestBody Asiento asiento) {
        ResultadoOperacion<Asiento> resultado = serviceAsiento.RegistrarAsiento(asiento);

        if (resultado.isSuccess()) {
            return ResponseEntity.ok(resultado);
        } else {
            HttpStatus status = switch (resultado.getErrorCode()) {
                case "DATOS_INVALIDOS" -> HttpStatus.BAD_REQUEST;
                case "ERROR_PERSISTENCIA" -> HttpStatus.INTERNAL_SERVER_ERROR;
                default -> HttpStatus.BAD_REQUEST;
            };
            return ResponseEntity.status(status).body(resultado);
        }
    }
}
