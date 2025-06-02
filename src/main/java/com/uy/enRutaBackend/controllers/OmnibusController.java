package com.uy.enRutaBackend.controllers;

import com.uy.enRutaBackend.datatypes.DtOmnibus;
import com.uy.enRutaBackend.errors.ErrorCode;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.icontrollers.IServiceOmnibus;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/omnibus")
//@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class OmnibusController {

	@Autowired
	private IServiceOmnibus omnibusService;

    @PostMapping("/registrar")
    public ResponseEntity<ResultadoOperacion<DtOmnibus>> registrarOmnibus(@RequestBody DtOmnibus dto) {
        ResultadoOperacion<DtOmnibus> resultado = omnibusService.registrarOmnibus(dto);

        if (resultado.isSuccess()) {
            return ResponseEntity.ok(resultado); // 200 OK
        } else {
            return ResponseEntity.badRequest().body(resultado); // 400 Bad Request
        }
    }
    
    @GetMapping("/listar")
    public ResponseEntity<ResultadoOperacion<List<DtOmnibus>>> listarOmnibus() {
        ResultadoOperacion<List<DtOmnibus>> resultado = omnibusService.listarOmnibus();

        if (!resultado.isSuccess()) {
            if (resultado.getErrorCode() == ErrorCode.LISTA_VACIA) {
                return ResponseEntity.status(204).body(resultado); // 204 No Content
            } else {
                return ResponseEntity.status(500).body(resultado); // 500 Error interno
            }
        }

        return ResponseEntity.ok(resultado); // 200 OK
    }
}
