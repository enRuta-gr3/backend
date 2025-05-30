package com.uy.enRutaBackend.controllers;

import com.uy.enRutaBackend.datatypes.DtOmnibus;
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
    public ResponseEntity<List<DtOmnibus>> listarOmnibus() {
        List<DtOmnibus> omnibus = omnibusService.listarOmnibus();
        return ResponseEntity.ok(omnibus);
    }
}
