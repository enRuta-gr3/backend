package com.uy.enRutaBackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uy.enRutaBackend.errors.ErrorCode;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.icontrollers.IServiceViaje;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/estadisticas")
public class EstadisticasController {

	private final IServiceViaje serviceViaje;

	@Autowired
	public EstadisticasController(IServiceViaje serviceViaje) {
		this.serviceViaje = serviceViaje;
	}
	
	@GetMapping("/viajesPorLocalidad")
	@Operation(summary = "Devuelve cuantos viajes se dieron de alta para cada localidad.")
	@Hidden
	public ResponseEntity<?> viajesPorLocalidad(@RequestParam int anio) {
		ResultadoOperacion<?> res = serviceViaje.calcularCantidadViajesLocalidad(anio);
		if (res != null && res.isSuccess()) {
			System.out.println("*ESTADISTICAS - Viajes/Localidad* " + res.getMessage());
			//System.out.println("*ESTADISTICAS - Viajes/Localidad* " + res.getData());
			return ResponseEntity.ok(res);
		} else {
			if (res.getErrorCode() == ErrorCode.LISTA_VACIA) {
				System.out.println("*ESTADISTICAS - Viajes/Localidad* " + res.getMessage());
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(res);
			} else {
				System.out.println("*ESTADISTICAS - Viajes/Localidad* " + res.getMessage());
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
			}
		}
	}
}
