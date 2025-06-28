package com.uy.enRutaBackend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uy.enRutaBackend.datatypes.DtPasaje;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.icontrollers.IServiceOmnibus;
import com.uy.enRutaBackend.icontrollers.IServicePasaje;
import com.uy.enRutaBackend.icontrollers.IServiceVendedor;
import com.uy.enRutaBackend.icontrollers.IServiceViaje;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/vendedor")
public class VendedorController {

	private final IServiceViaje serviceViaje;
	private final IServiceVendedor serviceVendedor;
	private final IServiceOmnibus serviceOmnibus;
	private final IServicePasaje servicePasaje;

	@Autowired
	public VendedorController(IServiceViaje serviceViaje, IServiceVendedor serviceVendedor, 
			IServiceOmnibus serviceOmnibus, IServicePasaje servicePasaje) {
		this.serviceViaje = serviceViaje;
		this.serviceVendedor = serviceVendedor;
		this.serviceOmnibus = serviceOmnibus;
		this.servicePasaje = servicePasaje;
	}
	
	@PostMapping("/devolverPasajes")
	@Operation(summary = "Permite devolver pasajes previamente comprados.")
	public ResponseEntity<?> devolverPasajes(@RequestBody List<DtPasaje> pasajes) {
		ResultadoOperacion<?> res = serviceVendedor.devolverPasajes(pasajes);
		if (res != null && res.isSuccess()) {
			System.out.println("*DEVOLUCION - * " + res.getMessage());
			return ResponseEntity.ok(res);
		} else {
			System.out.println("*DEVOLUCION - * " + res.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
		}
	}

	@GetMapping("/viajesPorLocalidad")
	@Operation(summary = "Devuelve cuantos viajes partieron de cada localidad para un determinado mes.")
	public ResponseEntity<?> viajesPorLocalidad(@RequestParam int anio, @RequestParam int mes) {
		ResultadoOperacion<?> res = serviceViaje.calcularCantidadViajesLocalidad(anio, mes);
		if (res != null && res.isSuccess()) {
			System.out.println("*ESTADISTICAS - Viajes/Localidad* " + res.getMessage());
			return ResponseEntity.ok(res);
		} else {
			System.out.println("*ESTADISTICAS - Viajes/Localidad* " + res.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
		}
	}
	
	@GetMapping("/viajesPorMes")
	@Operation(summary = "Devuelve cuantos viajes se realizan por mes para un año especifico.")
	public ResponseEntity<?> viajesPorMes(@RequestParam int anio) {
		ResultadoOperacion<?> res = serviceViaje.calcularCantidadViajesPorMesAlAnio(anio);
		if (res != null && res.isSuccess()) {
			System.out.println("*ESTADISTICAS - Viajes/Localidad* " + res.getMessage());
			return ResponseEntity.ok(res);
		} else {
			System.out.println("*ESTADISTICAS - Viajes/Mes* " + res.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
		}
	}
	
	@GetMapping("/porcentajeOmnibusAsignados")
	@Operation(summary = "Devuelve el porcentaje de ómnibus asignados sobre el total de la flota")
	public ResponseEntity<?> porcentajeAsignados(){
		ResultadoOperacion<?> res = serviceOmnibus.calcularPorcentajeAsignados();
		if (res != null && res.isSuccess()) {
			System.out.println("*ESTADISTICAS - Porcentaje de asignacion de omnibus* " + res.getMessage());
			return ResponseEntity.ok(res);
		} else {
			System.out.println("*ESTADISTICAS - Porcentaje de asignacion de omnibus* " + res.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
		}
	}
	
	@GetMapping("/estadoOmnibusPorMes")
	@Operation(summary = "Devuelve comparativa de estados de los omnibus por mes, en el ultimo año")
	public ResponseEntity<?> omnibusPorEstadoPorMes() {
		ResultadoOperacion<?> res = serviceOmnibus.omnibusPorEstadoPorMes();
		if (res != null && res.isSuccess()) {
			System.out.println("*ESTADISTICAS - Omnibus por estado, por mes* " + res.getMessage());
			return ResponseEntity.ok(res);
		} else {
			System.out.println("*ESTADISTICAS - Omnibus por estado, por mes* " + res.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
		}
	}
	
	@GetMapping("/pasajesVendidosPorMes")
	@Operation(summary = "Muestra estadistica de pasajes vendidos por mes.")
	public ResponseEntity<?> pasajesVendidosPorMes() {
		ResultadoOperacion<?> res = servicePasaje.pasajesVendidosPorMes();
		if (res != null && res.isSuccess()) {
			System.out.println("*ESTADISTICAS - Pasajes vendidos por mes* " + res.getMessage());
			return ResponseEntity.ok(res);
		} else {
			System.out.println("*ESTADISTICAS - Pasajes vendidos por mes* " + res.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
		}
	}
	
	@GetMapping("/pasajesDevueltosPorMes")
	@Operation(summary = "Muestra estadistica de pasajes devueltos por mes.")
	public ResponseEntity<?> pasajesDevueltosPorMes() {
		ResultadoOperacion<?> res = servicePasaje.pasajesDevueltosPorMes();
		if (res != null && res.isSuccess()) {
			System.out.println("*ESTADISTICAS - Pasajes vendidos por mes* " + res.getMessage());
			return ResponseEntity.ok(res);
		} else {
			System.out.println("*ESTADISTICAS - Pasajes vendidos por mes* " + res.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
		}
	}
}