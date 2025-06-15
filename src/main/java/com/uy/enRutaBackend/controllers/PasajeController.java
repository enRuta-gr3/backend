package com.uy.enRutaBackend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uy.enRutaBackend.datatypes.DtUsuario;
import com.uy.enRutaBackend.entities.Venta_Compra;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.icontrollers.IServicePasaje;
import com.uy.enRutaBackend.icontrollers.IServiceVenta_Compra;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/pasajes")
public class PasajeController {
	
	private final IServiceVenta_Compra serviceVenta;
	private final IServicePasaje servicePasaje;
	
	@Autowired
	public PasajeController(IServiceVenta_Compra serviceVenta, IServicePasaje servicePasaje) {
		this.serviceVenta = serviceVenta;
		this.servicePasaje = servicePasaje;
	}


	@PostMapping("/solicitarHistorialPasajes")
	@Operation(summary = "Devuelve el historial de pasajes de un usuario")
	public ResponseEntity<?> solicitarHistorialPasajes (@RequestBody DtUsuario usuario) {
		List<Venta_Compra> comprasCliente = serviceVenta.listarVentas(usuario);
		if(comprasCliente != null && !comprasCliente.isEmpty()) {
			ResultadoOperacion<?> res = servicePasaje.solicitarHistorial(comprasCliente);
			if(res.isSuccess()) {
				System.out.println("*PASAJES* - Se listan los pasajes para el usuario indicado.");
				return ResponseEntity.ok(res);			
			} else {
				System.out.println("*PASAJES* - " + res.getMessage());
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
			}
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El usuario no ha comprado pasajes aún.");
		}
		
	}

}
