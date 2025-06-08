package com.uy.enRutaBackend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uy.enRutaBackend.datatypes.DtPasaje;
import com.uy.enRutaBackend.datatypes.DtVentaCompraResponse;
import com.uy.enRutaBackend.datatypes.DtVenta_Compra;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.icontrollers.IServiceVenta_Compra;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("api/venta")
public class Venta_CompraController {
	
    @Autowired
    private IServiceVenta_Compra ventaService;

    @PostMapping("/calcularVenta")
    public ResponseEntity<?> calcularVenta(@RequestBody List<DtPasaje> request) {
        DtVentaCompraResponse resultado = ventaService.calcularVenta(request);

        ResultadoOperacion<DtVentaCompraResponse> response = new ResultadoOperacion<>(
            true,
            "Procesado Correctamente",
            resultado
        );

        return ResponseEntity.ok(response);
    }
    
	@PostMapping("/confirmarVenta")
	@Operation(summary = "Actualiza el estado final de la venta y del pago.")
	public ResponseEntity<?> completarVenta(@RequestBody DtVenta_Compra compra) {
		ResultadoOperacion<?> res = ventaService.finalizarVenta(compra);
		if(res.isSuccess()) {
			System.out.println("*VENTA* - Venta completada correctamente.");
			return ResponseEntity.ok(res);			
		} else {
			System.out.println("*VENTA* - Error completando la venta.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
		}
	}
}
