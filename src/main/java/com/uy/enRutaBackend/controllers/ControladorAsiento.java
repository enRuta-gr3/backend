package com.uy.enRutaBackend.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uy.enRutaBackend.datatypes.DtDisAsiento;
import com.uy.enRutaBackend.datatypes.DtViaje;
import com.uy.enRutaBackend.errors.ErrorCode;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.icontrollers.IServiceAsiento;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/asientos")
public class ControladorAsiento {

    private final IServiceAsiento serviceAsiento;

    public ControladorAsiento(IServiceAsiento serviceAsiento) {
        this.serviceAsiento = serviceAsiento;
    }

    @PostMapping("/listarAsientos")
    @Operation(summary = "Lista los asientos de un ómnibus asociado a un viaje.")
    public ResponseEntity<?> listarAsientos(@RequestBody DtViaje viaje) {
    	ResultadoOperacion<?> res = serviceAsiento.listarAsientosDeOmnibus(viaje);
    	if (res != null && res.isSuccess()) {
			System.out.println("*ASIENTOS - listar por omnibus* " + res.getMessage());
//			System.out.println("*ASIENTOS - listar por omnibus* " + res.getData());
			return ResponseEntity.ok(res);
		} else {
			if (res.getErrorCode() == ErrorCode.LISTA_VACIA) {
				System.out.println("*ASIENTOS - listar por omnibus* " + res.getMessage());
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(res);
			} else {
				System.out.println("*ASIENTOS - listar por omnibus* " + res.getMessage());
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
			}
		}
    }
    
    @PostMapping("/cambiarEstado")
    @Operation(summary = "Marca asientos como ocupados cuando son seleccionados")
    public ResponseEntity<?> cambiarEstadoAsientos(@RequestBody List<DtDisAsiento> asientos) {
    	ResultadoOperacion<?> res = serviceAsiento.cambiarEstadoDisponibilidad(asientos);
    	if (res != null && res.isSuccess()) {
			System.out.println("*ASIENTOS - bloquear/desbloquear* " + res.getMessage());
			//System.out.println("*ASIENTOS - listar por omnibus* " + res.getData());
			return ResponseEntity.ok(res);
		} else if(res.getErrorCode().equals("Algunos asientos están ocupados.")){
			System.out.println("*ASIENTOS - bloquear/desbloquear* " + res.getMessage());
			return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(res);
		} else {
			System.out.println("*ASIENTOS - bloquear/desbloquear* " + res.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
		}
    }
}
