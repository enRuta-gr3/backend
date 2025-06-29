package com.uy.enRutaBackend.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uy.enRutaBackend.datatypes.DtHistoricoEstado;
import com.uy.enRutaBackend.datatypes.DtOmnibus;
import com.uy.enRutaBackend.errors.ErrorCode;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.icontrollers.IServiceOmnibus;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("api/omnibus")
//@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class OmnibusController {

    private final IServiceOmnibus omnibusService;

    public OmnibusController(IServiceOmnibus omnibusService) {
        this.omnibusService = omnibusService;
    }


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
    
    @PostMapping("/cambiarEstado")
    public ResponseEntity<ResultadoOperacion<?>> cambiarEstado(@RequestBody DtHistoricoEstado dto) {
        ResultadoOperacion<?> resultado = omnibusService.cambiarEstadoOmnibus(dto);
        return resultado.isSuccess() ? ResponseEntity.ok(resultado) : ResponseEntity.badRequest().body(resultado);
    }

    @GetMapping("/listarOmibusDisponiblesViaje")
	@Operation(summary = "Lista los omnibus disponibles para un viaje en específico")
	public ResponseEntity<?> listarOmibusDisponibles(@RequestParam int idViaje) {
    	ResultadoOperacion<?> resultado = omnibusService.buscarOmnibusDisponibles(idViaje);
    	if(resultado.isSuccess()) {
    		return ResponseEntity.ok(resultado);
    	} else {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado);
    	}
    }

   
}
